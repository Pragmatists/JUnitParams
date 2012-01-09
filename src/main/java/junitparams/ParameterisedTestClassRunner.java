package junitparams;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.model.*;

/**
 * Testclass-level functionalities to handle parameters from a JUnit runner
 * class.
 * 
 * @author Pawel Lipinski
 * 
 */
public class ParameterisedTestClassRunner {

    private Map<TestMethod, ParameterisedTestMethodRunner> parameterisedMethods = new HashMap<TestMethod, ParameterisedTestMethodRunner>();
    private Map<FrameworkMethod, TestMethod> testMethods = new HashMap<FrameworkMethod, TestMethod>();
    private List<TestMethod> testMethodsList;

    public ParameterisedTestClassRunner(TestClass testClass) {
        testMethodsList = TestMethod.listFrom(testClass.getAnnotatedMethods(Test.class), testClass);
        fillTestMethodsMap();
        computeFrameworkMethods(false);
    }

    private void fillTestMethodsMap() {
        for (TestMethod testMethod : testMethodsList)
            testMethods.put(testMethod.frameworkMethod, testMethod);
    }

    /**
     * Returns a list of <code>FrameworkMethod</code>s. Handles both
     * parameterised methods (counts them as many times as many paramsets they
     * have) and nonparameterised methods (just counts them once).
     * 
     * @param list
     *            List of FrameworkMethods that should be taken into account.
     * @param firstTimeJustToGetNames
     *            If true, returns parameterised methods only once.
     * @return
     */
    public List<FrameworkMethod> computeFrameworkMethods(boolean firstTimeJustToGetNames) {
        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();

        for (TestMethod testMethod : testMethodsList) {
            if (testMethod.isParameterised() && !firstTimeJustToGetNames)
                addTestMethodForEachParamSet(resultMethods, testMethod);
            else
                addTestMethodOnce(resultMethods, testMethod);

            if (firstTimeJustToGetNames)
                testMethod.warnIfNoParamsGiven();
        }

        return resultMethods;
    }

    private void addTestMethodForEachParamSet(List<FrameworkMethod> resultMethods, TestMethod testMethod) {
        ParameterisedTestMethodRunner parameterisedTestMethodRunner = createOrGetCachedMethodRunner(testMethod);
        if (testMethod.isNotIgnored()) {
            int paramSetSize = parameterisedTestMethodRunner.method.parametersSets().length;
            for (int i = 0; i < paramSetSize; i++)
                addTestMethodOnce(resultMethods, testMethod);
        } else {
            addTestMethodOnce(resultMethods, testMethod);
        }

        parameterisedMethods.put(testMethod, parameterisedTestMethodRunner);
    }

    private ParameterisedTestMethodRunner createOrGetCachedMethodRunner(TestMethod testMethod) {
        ParameterisedTestMethodRunner parameterisedTestMethodRunner;
        if (parameterisedMethods.containsKey(testMethod))
            parameterisedTestMethodRunner = parameterisedMethods.get(testMethod);
        else
            parameterisedTestMethodRunner = new ParameterisedTestMethodRunner(testMethod);
        return parameterisedTestMethodRunner;
    }

    private void addTestMethodOnce(List<FrameworkMethod> resultMethods, TestMethod testMethod) {
        resultMethods.add(testMethod.frameworkMethod);
    }

    /**
     * Returns a InvokeParameterisedMethod for parameterised methods and null
     * for nonparameterised
     * 
     * @param method
     *            Test method
     * @param testClass
     * @return
     */
    public Statement parameterisedMethodInvoker(FrameworkMethod method, Object testClass) {
        TestMethod testMethod = testMethods.get(method);

        if (!testMethod.isParameterised())
            return null;

        ParameterisedTestMethodRunner parameterisedMethod = parameterisedMethods.get(testMethod);

        return new InvokeParameterisedMethod(
                method, testClass, parameterisedMethod.currentParamsFromAnnotation(), parameterisedMethod.count());
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
     * @param method
     *            TODO
     * 
     * @return Description of a method or null if it's not parameterised.
     */
    public Description describeParameterisedMethod(FrameworkMethod method) {
        TestMethod testMethod = testMethods.get(method);

        if (!testMethod.isParameterised())
            return null;

        return testMethod.describe();
    }

    /**
     * Checks if a method has a <code>Parameters</code> annotation
     * 
     * @param parameterObject
     * @return
     */
    public boolean isParameterised(TestMethod parameterObject) {
        return parameterObject.isParameterised();
    }

    public TestMethod testMethodFor(FrameworkMethod method) {
        return testMethods.get(method);
    }
}
