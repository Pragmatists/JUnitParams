package junitparams;

import java.lang.reflect.*;
import java.util.*;

import javax.lang.model.type.*;

import org.junit.internal.runners.model.*;
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

        int paramSetSize = parameterisedTestMethodRunner.paramsFromAnnotation(this).length;

        for (int i = 0; i < paramSetSize; i++)
            addTestMethodOnce(resultMethods, testMethod);

        parameterisedMethods.put(testMethod, parameterisedTestMethodRunner);
    }

    private void addTestMethodOnce(List<FrameworkMethod> resultMethods, FrameworkMethod testMethod) {
        resultMethods.add(testMethod);
    }

    Object[] paramsFromMethod(Parameters parametersAnnotation, FrameworkMethod method) {
        String methodName = parametersAnnotation.method();
        try {
            Class<?> testClass = method.getMethod().getDeclaringClass();
            Object testObject = testClass.newInstance();
            if ("".equals(methodName))
                methodName = "parametersFor" + method.getName().substring(0, 1).toUpperCase()
                            + method.getName().substring(1);
            Method provideMethod = testClass.getDeclaredMethod(methodName);
            provideMethod.setAccessible(true);
            return (Object[]) provideMethod.invoke(testObject);
        } catch (Exception e) {
            throw new RuntimeException("Could not find method: " + methodName + " so no params were used.", e);
        }
    }

    Object[] paramsFromValue(Parameters parametersAnnotation) {
        Object[] params = parametersAnnotation.value();
        return params;
    }

    Object[] paramsFromSource(Parameters parametersAnnotation) {
        if (!(parametersAnnotation.source().isAssignableFrom(NullType.class))) {
            Class<?> sourceClass = parametersAnnotation.source();
            ArrayList<Object> result = new ArrayList<Object>();
            Method[] methods = sourceClass.getDeclaredMethods();

            for (Method method : methods) {
                if (method.getName().startsWith("provide")) {
                    if (!Modifier.isStatic(method.getModifiers()))
                        throw new RuntimeException("Parameters source method " +
                                method.getName() +
                                " is not declared as static. Modify it to a static method.");
                    try {
                        result.addAll(Arrays.asList((Object[]) method.invoke(null)));
                    } catch (Exception e) {
                        throw new RuntimeException("Cannot invoke parameters source method: " + method.getName(), e);
                    }
                }
            }

            if (result.isEmpty())
                throw new RuntimeException("No methods starting with provide or they return no result in the parameters source class: "
                        + sourceClass.getName());
            return result.toArray(new Object[] {});
        }
        return JUnitParamsRunner.$();
    }

    Statement parameterisedMethodInvoker(FrameworkMethod method, Object test) {
        if (!isParameterised(method))
            return null;

        ParameterisedTestMethodRunner parameterisedTestMethodRunner = parameterisedMethods.get(method);
        return new InvokeParameterisedMethod(method, test, parameterisedTestMethodRunner.currentParamsFromAnnotation(this));
    }

    boolean runParameterisedTest(FrameworkMethod method, Statement methodInvoker, RunNotifier notifier, EachTestNotifier eachNotifier) {
        if (!isParameterised(method))
            return false;

        Description methodDescription = describeParameterisedMethod(method);
        Description methodWithParams = findChildForParams(methodInvoker, methodDescription);

        notifier.fireTestStarted(methodWithParams);
        runMethodInvoker(notifier, eachNotifier, methodDescription, methodInvoker, methodWithParams);
        notifier.fireTestFinished(methodWithParams);
        return true;
    }

    Description describeParameterisedMethod(FrameworkMethod method) {
        if (!isParameterised(method))
            return null;

        Object[] params = parameterisedMethods.get(method).paramsFromAnnotation(this);
        Description parametrised = Description.createSuiteDescription(method.getName());
        for (Object paramSet : params)
            parametrised.addChild(Description.createTestDescription(method.getMethod().getDeclaringClass(),
                    Utils.stringify(paramSet) + " (" + method.getName() + ")", method.getAnnotations()));
        return parametrised;
    }

    private Description findChildForParams(Statement methodInvoker, Description methodDescription) {
        for (Description child : methodDescription.getChildren()) {
            InvokeParameterisedMethod parameterisedInvoker = findParameterisedMethodInvokerInChain(methodInvoker);

            if (child.getMethodName().startsWith(parameterisedInvoker.getParamsAsString()))
                return child;
        }
        return null;
    }

    private InvokeParameterisedMethod findParameterisedMethodInvokerInChain(Statement methodInvoker) {
        while (methodInvoker != null && !(methodInvoker instanceof InvokeParameterisedMethod))
            methodInvoker = nextChainedInvoker(methodInvoker);

        if (methodInvoker == null)
            throw new RuntimeException("Cannot find invoker for the parameterised method. Using wrong JUnit version?");

        return (InvokeParameterisedMethod) methodInvoker;
    }

    private Statement nextChainedInvoker(Statement methodInvoker) {
        try {
            Field methodInvokerField = methodInvoker.getClass().getDeclaredField("fNext");
            methodInvokerField.setAccessible(true);
            return (Statement) methodInvokerField.get(methodInvoker);
        } catch (Exception e) {
            return null;
        }
    }

    private void runMethodInvoker(RunNotifier notifier, EachTestNotifier eachNotifier, Description description, Statement methodInvoker,
            Description methodWithParams) {
        try {
            eachNotifier.fireTestStarted();
            methodInvoker.evaluate();
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(methodWithParams, e));
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    private boolean isParameterised(FrameworkMethod method) {
        return method.getMethod().isAnnotationPresent(Parameters.class);
    }
}
