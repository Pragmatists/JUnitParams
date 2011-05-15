package junitparams;

import java.lang.reflect.*;
import java.util.*;

import javax.lang.model.type.*;

import org.junit.*;
import org.junit.internal.runners.model.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import org.junit.runners.model.*;

/**
 * JUnit runner for parameterised tests. Annotate your test class with
 * <code>@RunWith(JUnitParamsRunner.class)</code> and place
 * <code>@Parameters</code> annotation on each test method which requires
 * parameters.
 * 
 * @author Pawel Lipinski
 */
public class JUnitParamsRunner extends BlockJUnit4ClassRunner {

    private HashMap<FrameworkMethod, Integer> paramSetIndexMap;

    public JUnitParamsRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    protected void collectInitializationErrors(List<Throwable> errors) {
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        if (isParameterised(method))
            runParameterisedScenario(method, notifier);
        else
            super.runChild(method, notifier);
    }

    private void runParameterisedScenario(FrameworkMethod method, RunNotifier notifier) {
        Statement methodInvoker = methodBlock(method);

        Description methodDescription = describeParameterisedMethod(method);
        Description methodWithParams = findChildForParams(methodInvoker, methodDescription);

        notifier.fireTestStarted(methodWithParams);
        runMethodInvoker(notifier, methodDescription, methodInvoker, methodWithParams);
        notifier.fireTestFinished(methodWithParams);
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
    };

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return addTestMethods(getTestClass().getAnnotatedMethods(Test.class), false);
    }

    private List<FrameworkMethod> addTestMethods(List<FrameworkMethod> testMethods, boolean parameterisedOnlyOnce) {
        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();

        paramSetIndexMap = new HashMap<FrameworkMethod, Integer>();
        for (FrameworkMethod testMethod : testMethods) {
            if (isParameterised(testMethod) && !parameterisedOnlyOnce)
                addTestMethodForEachParamSet(resultMethods, testMethod);
            else
                addTestMethodOnce(resultMethods, testMethod);
        }

        return resultMethods;
    }

    private void addTestMethodForEachParamSet(List<FrameworkMethod> resultMethods, FrameworkMethod scenarioMethod) {
        int paramSetSize = paramsFromAnnotation(scenarioMethod).length;

        for (int i = 0; i < paramSetSize; i++)
            addTestMethodOnce(resultMethods, scenarioMethod);

        paramSetIndexMap.put(scenarioMethod, 0);
    }

    private void addTestMethodOnce(List<FrameworkMethod> resultMethods, FrameworkMethod scenarioMethod) {
        resultMethods.add(scenarioMethod);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        if (isParameterised(method)) {
            Integer counter = paramSetIndexMap.get(method);
            paramSetIndexMap.put(method, counter + 1);
            return new InvokeParameterisedMethod(method, test, paramsFromAnnotation(method)[counter]);
        } else {
            return super.methodInvoker(method, test);
        }
    }

    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription(getName(), getTestClass().getAnnotations());

        List<FrameworkMethod> resultMethods = addTestMethods(getTestClass().getAnnotatedMethods(Test.class), true);

        for (FrameworkMethod method : resultMethods)
            description.addChild(describeMethod(method));

        return description;
    }

    private Description describeMethod(FrameworkMethod method) {
        Description child;

        if (isParameterised(method))
            child = describeParameterisedMethod(method);
        else
            child = describeChild(method);
        return child;
    }

    private boolean isParameterised(FrameworkMethod method) {
        return method.getMethod().isAnnotationPresent(Parameters.class);
    }

    private Description describeParameterisedMethod(FrameworkMethod method) {
        Object[] params = paramsFromAnnotation(method);
        Description parametrised = Description.createSuiteDescription(testName(method));
        for (Object paramSet : params)
            parametrised.addChild(Description.createTestDescription(getTestClass().getJavaClass(),
                    Utils.stringify(paramSet) + " (" + testName(method) + ")", method.getAnnotations()));
        return parametrised;
    }

    private Object[] paramsFromAnnotation(FrameworkMethod method) {
        Parameters parametersAnnotation = method.getAnnotation(Parameters.class);
        Object[] params = paramsFromValue(parametersAnnotation);

        if (params.length == 0)
            params = paramsFromSource(parametersAnnotation);

        if (params.length == 0)
            params = paramsFromMethod(parametersAnnotation, method);

        if (params.length == 0)
            throw new RuntimeException(
                    "No parameters found, even though the method is defined as Prameterised. "
                            + "There aren't any params in the annotation, there's no test class method providing the params and no external provider...");

        return params;
    }

    private Object[] paramsFromMethod(Parameters parametersAnnotation, FrameworkMethod method) {
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

    private Object[] paramsFromValue(Parameters parametersAnnotation) {
        Object[] params = parametersAnnotation.value();
        return params;
    }

    private Object[] paramsFromSource(Parameters parametersAnnotation) {
        if (!(parametersAnnotation.source().isAssignableFrom(NullType.class))) {
            Class sourceClass = parametersAnnotation.source();
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
        return $();
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

    public static Object[] $(Object... params) {
        return params;
    }
}
