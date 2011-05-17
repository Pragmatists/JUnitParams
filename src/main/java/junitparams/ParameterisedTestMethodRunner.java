package junitparams;

import java.lang.reflect.*;
import java.util.*;

import javax.lang.model.type.*;

import org.junit.internal.runners.model.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.model.*;

public class ParameterisedTestMethodRunner {

    private int count;
    private final FrameworkMethod method;
    private Parameters parametersAnnotation;

    public ParameterisedTestMethodRunner(FrameworkMethod method) {
        this.method = method;
        parametersAnnotation = method.getAnnotation(Parameters.class);
    }

    public int nextCount() {
        return count++;
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

        ArrayList<Object> result = new ArrayList<Object>();
        Class<?> sourceClass = parametersAnnotation.source();

        while (sourceClass.getSuperclass() != null) {
            result.addAll(allProviderMethodsFromClass(sourceClass));
            sourceClass = sourceClass.getSuperclass();
        }

        if (result.isEmpty())
            throw new RuntimeException("No methods starting with provide or they return no result in the parameters source class: "
                        + sourceClass.getName());
        return result.toArray(new Object[] {});
    }

    private ArrayList<Object> allProviderMethodsFromClass(Class<?> sourceClass) {
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
        Class<?> testClass = method.getMethod().getDeclaringClass();
        Class<?> declaringClass = testClass;
        Method provideMethod = null;
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

        try {
            Object testObject = testClass.newInstance();
            provideMethod.setAccessible(true);
            return (Object[]) provideMethod.invoke(testObject);
        } catch (Exception e) {
            throw new RuntimeException("Could not invoke method: " + methodName + " defined in class " + testClass
                    + " so no params were used.", e);
        }
    }

    private String defaultMethodName() {
        String methodName;
        methodName = "parametersFor" + method.getName().substring(0, 1).toUpperCase()
                    + method.getName().substring(1);
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
        Description parametrised = Description.createSuiteDescription(method.getName());
        for (Object paramSet : params)
            parametrised.addChild(Description.createTestDescription(method.getMethod().getDeclaringClass(),
                    Utils.stringify(paramSet) + " (" + method.getName() + ")", method.getAnnotations()));
        return parametrised;
    }

    private void runMethodInvoker(RunNotifier notifier, Description description, Statement methodInvoker, Description methodWithParams) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
        try {
            eachNotifier.fireTestStarted();
            methodInvoker.evaluate();
        } catch (Throwable e) {
            notifier.fireTestFailure(new Failure(methodWithParams, e));
        } finally {
            eachNotifier.fireTestFinished();
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
        try {
            Field methodInvokerField = methodInvoker.getClass().getDeclaredField("fNext");
            methodInvokerField.setAccessible(true);
            return (Statement) methodInvokerField.get(methodInvoker);
        } catch (Exception e) {
            return null;
        }
    }
}
