package junitparams;

import java.util.*;

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

    /**
     * Returns a list of <code>FrameworkMethod</code>s. Handles both
     * parameterised methods (counts them as many times as many paramsets they
     * have) and nonparameterised methods (just counts them once).
     * 
     * @param list
     *            List of FrameworkMethods that should be taken into account.
     * @param parameterisedOnlyOnce
     *            If true, returns parameterised methods only once.
     * @return
     */
    public List<FrameworkMethod> computeTestMethods(List<TestMethod> list, boolean parameterisedOnlyOnce) {
        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();

        for (TestMethod testMethod : list) {
            if (isParameterised(testMethod) && !parameterisedOnlyOnce)
                addTestMethodForEachParamSet(resultMethods, testMethod);
            else
                addTestMethodOnce(resultMethods, testMethod);
        }

        return resultMethods;
    }

    private void addTestMethodForEachParamSet(List<FrameworkMethod> resultMethods, TestMethod testMethod) {
        ParameterisedTestMethodRunner parameterisedTestMethodRunner = new ParameterisedTestMethodRunner(testMethod);

        int paramSetSize = parameterisedTestMethodRunner.paramsFromAnnotation().length;

        for (int i = 0; i < paramSetSize; i++)
            addTestMethodOnce(resultMethods, testMethod);

        parameterisedMethods.put(testMethod, parameterisedTestMethodRunner);
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
    public Statement parameterisedMethodInvoker(TestMethod method, Object testClass) {
        if (!isParameterised(method))
            return null;

        ParameterisedTestMethodRunner parameterisedMethod = parameterisedMethods.get(method);
        return new InvokeParameterisedMethod(method.frameworkMethod, testClass, parameterisedMethod.currentParamsFromAnnotation(),
                parameterisedMethod.count());
    }

    /**
     * Executes parameterised method.
     * 
     * @param method
     * @param methodInvoker
     * @param notifier
     * @return True if method has been executed, and false if not.
     */
    public boolean runParameterisedTest(TestMethod method, Statement methodInvoker, RunNotifier notifier) {
        if (!isParameterised(method))
            return false;

        parameterisedMethods.get(method).runTestMethod(methodInvoker, notifier);
        return true;
    }

    /**
     * Returns description of a parameterised method.
     * 
     * @param testMethod
     *            TODO
     * 
     * @return Description of a method or null if it's not parameterised.
     */
    public Description describeParameterisedMethod(TestMethod testMethod) {
        if (!isParameterised(testMethod))
            return null;

        return parameterisedMethods.get(testMethod).describeMethod();
    }

    /**
     * Checks if a method has a <code>Parameters</code> annotation
     * 
     * @param parameterObject
     * @return
     */
    public boolean isParameterised(TestMethod parameterObject) {
        return parameterObject.frameworkMethod.getMethod().isAnnotationPresent(Parameters.class);
    }
}
