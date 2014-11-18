package junitparams.testnaming;

public interface TestCaseNamingStrategy {
    String getTestCaseName(int parametersIndex, Object parameters);
}
