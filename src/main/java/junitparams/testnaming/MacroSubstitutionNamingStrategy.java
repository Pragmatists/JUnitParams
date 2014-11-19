package junitparams.testnaming;

import junitparams.internal.TestMethod;
import junitparams.internal.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.regex.Pattern;

public class MacroSubstitutionNamingStrategy implements TestCaseNamingStrategy {
    private static final String MACRO_PATTERN = "\\{[^\\}]{0,50}\\}";
    // Pattern that keeps delimiters in split result
    private static final Pattern MACRO_SPLIT_PATTERN = Pattern.compile(String.format("(?=%s)|(?<=%s)", MACRO_PATTERN, MACRO_PATTERN));
    private static final String MACRO_START = "{";
    private static final String MACRO_END = "}";
    static final String DEFAULT_TEMPLATE = "[{index}] {params} ({method})";
    private TestMethod method;

    public MacroSubstitutionNamingStrategy(TestMethod testMethod) {
        this.method = testMethod;
    }

    @Override
    public String getTestCaseName(int parametersIndex, Object parameters) {
        TestCaseName testCaseName = method.getAnnotation(TestCaseName.class);

        String template = getTemplate(testCaseName);

        return buildNameByTemplate(template, parametersIndex, parameters);
    }

    private String getTemplate(TestCaseName testCaseName) {
        if (testCaseName != null && !testCaseName.value().trim().isEmpty()) {
            return testCaseName.value();
        }

        return DEFAULT_TEMPLATE;
    }

    private String buildNameByTemplate(String template, int parametersIndex, Object parameters) {
        StringBuilder nameBuilder = new StringBuilder();

        String[] parts = MACRO_SPLIT_PATTERN.split(template);

        for (String part : parts) {
            String transformedPart = transformPart(part, parametersIndex, parameters);
            nameBuilder.append(transformedPart);
        }

        return nameBuilder.toString();
    }

    private String transformPart(String part, int parametersIndex, Object parameters) {
        if (isMacro(part)) {
            return lookupMacroValue(part, parametersIndex, parameters);
        }

        return part;
    }

    private String lookupMacroValue(String macro, int parametersIndex, Object parameters) {
        String macroKey = getMacroKey(macro);

        switch (Macro.parse(macroKey)) {
            case INDEX: return String.valueOf(parametersIndex);
            case PARAMS: return Utils.stringify(parameters);
            case METHOD: return method.name();
            default: return substituteDynamicMacro(macro, macroKey, parameters);
        }
    }

    private String substituteDynamicMacro(String macro, String macroKey, Object parameters) {
        if (isMethodParameterIndex(macroKey)) {
            int index = parseIndex(macroKey);
            return Utils.getParameterStringByIndexOrEmpty(parameters, index);
        }

        return macro;
    }

    private boolean isMethodParameterIndex(String macroKey) {
        return macroKey.matches("\\d+");
    }

    private int parseIndex(String macroKey) {
        return Integer.parseInt(macroKey);
    }

    private String getMacroKey(String macro) {
        return macro
                .substring(MACRO_START.length(), macro.length() - MACRO_END.length())
                .toUpperCase(Locale.ENGLISH);
    }

    private boolean isMacro(String part) {
        return part.startsWith(MACRO_START) && part.endsWith(MACRO_END);
    }

    private enum Macro {
        INDEX,
        PARAMS,
        METHOD,
        NONE;

        public static Macro parse(String value) {
            if (macros.contains(value)) {
                return Macro.valueOf(value);
            } else {
                return Macro.NONE;
            }
        }

        private static final HashSet<String> macros = new HashSet<>(Arrays.asList(
                Macro.INDEX.toString(), Macro.PARAMS.toString(), Macro.METHOD.toString())
        );
    }
}
