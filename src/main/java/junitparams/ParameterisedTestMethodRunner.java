package junitparams;

import java.lang.reflect.*;
import java.util.*;

import javax.lang.model.type.*;

import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.model.*;

/**
 * Testmethod-level functionalities for parameterised tests
 * 
 * @author Pawel Lipinski
 * 
 */
public class ParameterisedTestMethodRunner {

    private int count;
    private final TestMethod method;
    private Parameters parametersAnnotation;

    public ParameterisedTestMethodRunner(TestMethod testMethod) {
        this.method = testMethod;
        parametersAnnotation = testMethod.frameworkMethod.getAnnotation(Parameters.class);
    }

    public int nextCount() {
        return count++;
    }

    public int count() {
        return count;
    }

    Object[] paramsFromAnnotation() {
        Object[] params = paramsFromValue();

        if (params.length == 0)
            params = paramsFromSource();

        if (params.length == 0)
            params = paramsFromMethod();

        if (params.length == 0)
            throw new RuntimeException(
                    "No parameters found, even though the method is defined as Prameterised. "
                            + "There aren't any params in the annotation, there's no test class method providing the params and no external provider...");

        return params;
    }

    private Object[] paramsFromValue() {
        Object[] params = parametersAnnotation.value();
        return params;
    }

    private Object[] paramsFromSource() {
        if (sourceClassUndefined())
            return new Object[] {};

        Class<?> sourceClass = parametersAnnotation.source();

        return fillResultWithAllParamProviderMethods(sourceClass);
    }

    private Object[] fillResultWithAllParamProviderMethods(Class<?> sourceClass) {
        ArrayList<Object> result = new ArrayList<Object>();
        while (sourceClass.getSuperclass() != null) {
            result.addAll(gatherParamsFromAllMethodsFrom(sourceClass));
            sourceClass = sourceClass.getSuperclass();
        }
        if (result.isEmpty())
            throw new RuntimeException("No methods starting with provide or they return no result in the parameters source class: "
                        + sourceClass.getName());
        return result.toArray(new Object[] {});
    }

    private ArrayList<Object> gatherParamsFromAllMethodsFrom(Class<?> sourceClass) {
        ArrayList<Object> result = new ArrayList<Object>();
        Method[] methods = sourceClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().startsWith("provide")) {
                if (!Modifier.isStatic(method.getModifiers()))
                    throw new RuntimeException("Parameters source method " +
                                method.getName() +
                                " is not declared as static. Modify it to a static method.");
                try {
                    result.addAll(Arrays.asList(processParamsIfSingle((Object[]) method.invoke(null))));
                } catch (Exception e) {
                    throw new RuntimeException("Cannot invoke parameters source method: " + method.getName(), e);
                }
            }
        }
        return result;
    }

    private boolean sourceClassUndefined() {
        return parametersAnnotation.source().isAssignableFrom(NullType.class);
    }

    private Object[] paramsFromMethod() {
        String methodName = parametersAnnotation.method();
        if ("".equals(methodName))
            methodName = defaultMethodName();

        return invokeMethodWithParams(methodName);
    }

    private Object[] invokeMethodWithParams(String methodName) {
        Class<?> testClass = method.frameworkMethod.getMethod().getDeclaringClass();

        Method provideMethod = findParamsProvidingMethodInTestclassHierarchy(methodName, testClass);

        return invokeParamsProvidingMethod(testClass, provideMethod);
    }

    private Object[] invokeParamsProvidingMethod(Class<?> testClass, Method provideMethod) {
        try {
            Object testObject = testClass.newInstance();
            provideMethod.setAccessible(true);
            Object[] params = (Object[]) provideMethod.invoke(testObject);
            return processParamsIfSingle(params);
        } catch (Exception e) {
            throw new RuntimeException("Could not invoke method: " + provideMethod.getName() + " defined in class " + testClass
                    + " so no params were used.", e);
        }
    }

    private Object[] processParamsIfSingle(Object[] params) {
        if (method.frameworkMethod.getMethod().getParameterTypes().length != params.length)
            return params;

        if (params.length == 0)
            return params;

        Object param = params[0];
        if (param == null || !param.getClass().isArray())
            return new Object[] { params };

        return params;
    }

    private Method findParamsProvidingMethodInTestclassHierarchy(String methodName, Class<?> testClass) {
        Method provideMethod = null;
        Class<?> declaringClass = testClass;
        while (declaringClass.getSuperclass() != null) {
            try {
                provideMethod = declaringClass.getDeclaredMethod(methodName);
                break;
            } catch (Exception e) {
            }
            declaringClass = declaringClass.getSuperclass();
        }
        if (provideMethod == null)
            throw new RuntimeException("Could not find method: " + methodName + " so no params were used.");
        return provideMethod;
    }

    private String defaultMethodName() {
        String methodName;
        methodName = "parametersFor" + method.frameworkMethod.getName().substring(0, 1).toUpperCase()
                    + method.frameworkMethod.getName().substring(1);
        return methodName;
    }

    Object currentParamsFromAnnotation() {
        return paramsFromAnnotation()[nextCount()];
    }

    void runTestMethod(Statement methodInvoker, RunNotifier notifier) {
        Description methodDescription = describeMethod();
        Description methodWithParams = findChildForParams(methodInvoker, methodDescription);

        notifier.fireTestStarted(methodWithParams);
        runMethodInvoker(notifier, methodDescription, methodInvoker, methodWithParams);
        notifier.fireTestFinished(methodWithParams);
    }

    Description describeMethod() {
        Object[] params = paramsFromAnnotation();
        Description parametrised = Description.createSuiteDescription(method.name());
        for (int i = 0; i < params.length; i++) {
            Object paramSet = params[i];
            parametrised.addChild(Description.createTestDescription(method.frameworkMethod.getMethod().getDeclaringClass(),
                    Utils.stringify(paramSet, i) + " (" + method.name() + ")", method.frameworkMethod.getAnnotations()));
        }
        return parametrised;
    }

    private void runMethodInvoker(RunNotifier notifier, Description description, Statement methodInvoker, Description methodWithParams) {
        try {
            methodInvoker.evaluate();
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(methodWithParams, e));
        }
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
        Statement nextInvoker = null;
        try {
            nextInvoker = getFieldValue(methodInvoker, "fNext"); // most
                                                                 // invokers
        } catch (Exception e) {
            try {
                nextInvoker = getFieldValue(methodInvoker, "val$base"); // ExternalResource
            } catch (Exception e1) {
                // nothing found, will return null
            }
        }
        return nextInvoker;
    }

    private Statement getFieldValue(Statement methodInvoker, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field methodInvokerField = methodInvoker.getClass().getDeclaredField(fieldName);
        methodInvokerField.setAccessible(true);
        return (Statement) methodInvokerField.get(methodInvoker);
    }
}
