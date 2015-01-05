package junitparams.naming;

/**
 * A strategy that can resolve a test case method name by it's parameters.
 */
public interface TestCaseNamingStrategy {
    String getTestCaseName(int parametersIndex, Object parameters);
}
