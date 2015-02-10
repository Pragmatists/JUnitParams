package junitparams.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * Testclass-level functionalities to handle parameters from a JUnit runner
 * class.
 *
 * @author Pawel Lipinski
 */
public class ParameterizedTestMethodProvider {

    protected Map<TestMethod, ParametrisedTestMethodRunner> parameterisedMethods = new HashMap<TestMethod, ParametrisedTestMethodRunner>();
    protected List<TestMethod> testMethodsList;

    /**
     * Creates a runner for a given test class. Computes all the test methods
     * that are annotated as tests. Retrieves and caches all parameter values.
     *
     * @param testClass
     */
    public ParameterizedTestMethodProvider(TestClass testClass) {
        computeTestMethods(testClass);
        computeFrameworkMethods();
    }

    protected void computeTestMethods(TestClass testClass) {
        testMethodsList = TestMethod.listFrom(testClass.getAnnotatedMethods(Test.class), testClass);
    }

    /**
     * Returns a list of <code>FrameworkMethod</code>s. Handles both
     * parameterised methods (counts them as many times as many paramsets they
     * have) and nonparameterised methods (just counts them once).
     *
     * @return a list of FrameworkMethod objects
     */
    public List<FrameworkMethod> computeFrameworkMethods() {
        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();

        for (TestMethod testMethod : testMethodsList) {
            addTestMethodOnce(resultMethods, testMethod);
        }

        return resultMethods;
    }

    /**
     * Returns a list of <code>FrameworkMethod</code>s - once per method, like
     * there were no parameters.
     * For JUnit to build names for IDE.
     */
    public List<FrameworkMethod> returnListOfMethods() {
        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();

        for (TestMethod testMethod : testMethodsList) {
            addTestMethodOnce(resultMethods, testMethod);
            cacheMethodRunner(testMethod);
            testMethod.warnIfNoParamsGiven();
        }

        return resultMethods;
    }

    private void addTestMethodOnce(List<FrameworkMethod> resultMethods, TestMethod testMethod) {
        resultMethods.add(testMethod);
    }

    private void cacheMethodRunner(TestMethod testMethod) {
        if (!parameterisedMethods.containsKey(testMethod))
            parameterisedMethods.put(testMethod, new ParametrisedTestMethodRunner(testMethod));
    }

    /**
     * Returns a InvokeParameterisedMethod for parameterised methods and null
     * for nonparameterised
     *
     * @param method    Test method
     * @param testClass
     * @return a Statement with the invoker for the parameterised method
     */
    public Statement parameterisedMethodInvoker(TestMethod method, Object testClass) {
        if (!method.isParameterised())
            return null;

        return buildMethodInvoker(method, testClass, method);
    }

    private Statement buildMethodInvoker(TestMethod method, Object testClass, TestMethod testMethod) {
        ParametrisedTestMethodRunner parameterisedMethod = parameterisedMethods.get(testMethod);

        return new InvokeParameterisedMethod(method, testClass, parameterisedMethod.currentParamsFromAnnotation(), method.getIndex());
    }

    /**
     * Tells if method should be run by this runner.
     *
     * @param testMethod
     * @return true, iff testMethod should be run by this runner.
     */
    public boolean shouldRun(TestMethod testMethod) {
        return testMethod.isParameterised();
    }

    /**
     * Executes parameterised method.
     *
     * @param method
     * @param methodInvoker
     * @param notifier
     */
    public void runParameterisedTest(TestMethod method, Statement methodInvoker, RunNotifier notifier) {
        parameterisedMethods.get(method).runTestMethod(methodInvoker, notifier);
    }

    /**
     * Returns description of a parameterised method.
     *
     * @param method TODO
     * @return Description of a method or null if it's not parameterised.
     */
    public Description describeParameterisedMethod(FrameworkMethod method) {
        TestMethod testMethod = (TestMethod) method;

        if (!testMethod.isParameterised())
            return null;

        return testMethod.describe();
    }

}
