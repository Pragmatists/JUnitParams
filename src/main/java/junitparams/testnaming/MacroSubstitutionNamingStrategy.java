package junitparams.testnaming;

import junitparams.internal.TestMethod;
import junitparams.internal.Utils;

public class MacroSubstitutionNamingStrategy implements TestCaseNamingStrategy {
    private TestMethod method;

    public MacroSubstitutionNamingStrategy(TestMethod testMethod) {
        this.method = testMethod;
    }

    @Override
    public String getTestCaseName(int parametersIndex, Object parameters) {
        return createDefaultName(parametersIndex, parameters);
    }

    private String createDefaultName(int parametersIndex, Object parameters) {
        return Utils.stringify(parameters, parametersIndex) + " (" + method.name() + ")";
    }
}
