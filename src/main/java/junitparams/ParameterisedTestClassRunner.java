package junitparams;

import java.util.*;

import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.model.*;

public class ParameterisedTestClassRunner {

    private HashMap<FrameworkMethod, ParameterisedTestMethodRunner> parameterisedMethods = new HashMap<FrameworkMethod, ParameterisedTestMethodRunner>();

    List<FrameworkMethod> computeTestMethods(List<FrameworkMethod> testMethods, boolean parameterisedOnlyOnce) {
        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();

        for (FrameworkMethod testMethod : testMethods) {
            if (isParameterised(testMethod) && !parameterisedOnlyOnce)
                addTestMethodForEachParamSet(resultMethods, testMethod);
            else
                addTestMethodOnce(resultMethods, testMethod);
        }

        return resultMethods;
    }

    private void addTestMethodForEachParamSet(List<FrameworkMethod> resultMethods, FrameworkMethod testMethod) {
        ParameterisedTestMethodRunner parameterisedTestMethodRunner = new ParameterisedTestMethodRunner(testMethod);

        int paramSetSize = parameterisedTestMethodRunner.paramsFromAnnotation().length;

        for (int i = 0; i < paramSetSize; i++)
            addTestMethodOnce(resultMethods, testMethod);

        parameterisedMethods.put(testMethod, parameterisedTestMethodRunner);
    }

    private void addTestMethodOnce(List<FrameworkMethod> resultMethods, FrameworkMethod testMethod) {
        resultMethods.add(testMethod);
    }

    Statement parameterisedMethodInvoker(FrameworkMethod method, Object test) {
        if (!isParameterised(method))
            return null;

        ParameterisedTestMethodRunner parameterisedMethod = parameterisedMethods.get(method);
        return new InvokeParameterisedMethod(method, test, parameterisedMethod.currentParamsFromAnnotation(), parameterisedMethod.count());
    }

    boolean runParameterisedTest(FrameworkMethod method, Statement methodInvoker, RunNotifier notifier) {
        if (!isParameterised(method))
            return false;

        parameterisedMethods.get(method).runTestMethod(methodInvoker, notifier);
        return true;
    }

    Description describeParameterisedMethod(FrameworkMethod method) {
        if (!isParameterised(method))
            return null;

        return parameterisedMethods.get(method).describeMethod();
    }

    private boolean isParameterised(FrameworkMethod method) {
        return method.getMethod().isAnnotationPresent(Parameters.class);
    }
}
