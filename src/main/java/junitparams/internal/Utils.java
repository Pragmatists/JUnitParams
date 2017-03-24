package junitparams.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Some String utils to handle parameterised tests' results.
 *
 * @author Pawel Lipinski
 */
public class Utils {

    private static Map<Character, Character> SPECIAL_CHARACTERS_REPLACEMENTS = replacements();

    public static String stringify(Object paramSet, int paramIdx) {
        String result = "[" + paramIdx + "] ";

        return result + stringify(paramSet);
    }

    public static String stringify(Object paramSet) {
        String result;
        if (paramSet == null)
            result = "null";
        else if (paramSet instanceof String)
            result = paramSet.toString();
        else
            result = asCsvString(safelyCastParamsToArray(paramSet));

        return trimSpecialChars(result);
    }

    public static String getParameterStringByIndexOrEmpty(Object paramSet, int parameterIndex, boolean trimStringParameters) {
        Object[] params = safelyCastParamsToArray(paramSet);
        if (paramSet instanceof String) {
            params = splitAtCommaOrPipe((String) paramSet, trimStringParameters);
        }
        if (parameterIndex >= 0 && parameterIndex < params.length) {
            return addParamToResult("", params[parameterIndex]);
        }

        return "";
    }

    public static String[] splitAtCommaOrPipe(String input, boolean trimParameters) {
        ArrayList<String> result = new ArrayList<String>();

        char character = '\0';
        char previousCharacter;

        StringBuilder value = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            previousCharacter = character;
            character = input.charAt(i);

            if (character == ',' || character == '|') {
                if (previousCharacter == '\\') {
                    value.setCharAt(value.length() - 1, character);
                    continue;
                }
                result.add(trimParameters ? value.toString().trim() : value.toString());
                value = new StringBuilder();
                continue;
            }

            value.append(character);
        }
        result.add(trimParameters ? value.toString().trim() : value.toString());

        return result.toArray(new String[]{});
    }

    private static String trimSpecialChars(String result) {
        StringBuilder sb = new StringBuilder(result.length());

        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if (SPECIAL_CHARACTERS_REPLACEMENTS.containsKey(c)) {
                sb.append(SPECIAL_CHARACTERS_REPLACEMENTS.get(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static Map<Character, Character> replacements() {
        Map<Character, Character> replacements = new HashMap<Character, Character>();
        replacements.put('(', '[');
        replacements.put(')', ']');
        replacements.put('\n', ' ');
        replacements.put('\r', ' ');
        return replacements;
    }

    static Object[] safelyCastParamsToArray(Object paramSet) {
        final Object[] params;
        if (paramSet instanceof Object[]) {
            params = (Object[]) paramSet;
        } else {
            params = new Object[]{paramSet};
        }
        return params;
    }

    private static String asCsvString(Object[] params) {
        if (params == null)
            return "null";

        if (params.length == 0)
            return "";

        String result = "";

        for (int i = 0; i < params.length - 1; i++) {
            Object param = params[i];
            result = addParamToResult(result, param) + ", ";
        }
        result = addParamToResult(result, params[params.length - 1]);

        return result;
    }

    private static String addParamToResult(String result, Object param) {
        if (param == null)
            result += "null";
        else if (param.getClass().isArray())
            result += convertAnyArrayToString(param);
        else if (hasOverridenToStringMethod(param))
            result += param.toString();
        else
            result += param.getClass().getSimpleName();

        return result;
    }

    private static boolean hasOverridenToStringMethod(Object param) {
        Method[] methods = param.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("toString") && overridesMethod(method)) {
                return true;
            }
        }
        return false;
    }

    private static boolean overridesMethod(Method method) {
        return !method.getDeclaringClass().equals(Object.class);
    }

    static String uniqueMethodId(int index, Object paramSet, String methodName) {
        return stringify(paramSet, index) + " (" + methodName + ")";
    }

    private static String convertAnyArrayToString(Object arrayAsObject) {
        if (arrayAsObject.getClass().getComponentType().isPrimitive()) {
            return convertFromArrayOfPrimitives(arrayAsObject);
        } else {
            return Arrays.toString((Object[]) arrayAsObject);
        }
    }

    private static String convertFromArrayOfPrimitives(Object arrayOfPrimitives) {
        String componentType = arrayOfPrimitives.getClass().getComponentType().getName();
        if ("byte".equals(componentType)) {
            return Arrays.toString((byte[]) arrayOfPrimitives);
        } else if ("short".equals(componentType)) {
            return Arrays.toString((short[]) arrayOfPrimitives);
        } else if ("int".equals(componentType)) {
            return Arrays.toString((int[]) arrayOfPrimitives);
        } else if ("long".equals(componentType)) {
            return Arrays.toString((long[]) arrayOfPrimitives);
        } else if ("float".equals(componentType)) {
            return Arrays.toString((float[]) arrayOfPrimitives);
        } else if ("double".equals(componentType)) {
            return Arrays.toString((double[]) arrayOfPrimitives);
        } else if ("boolean".equals(componentType)) {
            return Arrays.toString((boolean[]) arrayOfPrimitives);
        } else {
            return Arrays.toString((char[]) arrayOfPrimitives);
        }
    }
}
