package pragmatists.parameterised;

import java.lang.reflect.*;
import java.util.*;

import javax.lang.model.type.*;

import org.junit.*;
import org.junit.internal.runners.model.*;
import org.junit.runner.*;
import org.junit.runner.notification.*;
import org.junit.runners.*;
import org.junit.runners.model.*;

public class ParameterisedTestsRunner extends BlockJUnit4ClassRunner {

    private HashMap<FrameworkMethod, Integer> paramSetIndexMap;

    public ParameterisedTestsRunner(Class<?> klass) throws InitializationError {
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
        Object[] params = paramsFromAnnotation(method);

        Description methodDescription = Description.createSuiteDescription(testName(method));
        for (Object paramSet : params)
            methodDescription.addChild(Description.createTestDescription(getTestClass().getJavaClass(),
                    Utils.stringify(paramSet) + " (" + testName(method) + ")", method.getAnnotations()));

        Description methodWithParams = findChildForParams(methodInvoker, methodDescription);

        notifier.fireTestStarted(methodWithParams);

        runMethodInvoker(notifier, methodDescription, methodInvoker, methodWithParams);

        notifier.fireTestFinished(methodWithParams);
    }

    private Object[] paramsFromAnnotation(FrameworkMethod method) {
        Parameters parametersAnnotation = method.getAnnotation(Parameters.class);
        Object[] params = parametersAnnotation.value();
        if (params.length == 0 && !(parametersAnnotation.source().isAssignableFrom(NullType.class)))
            params = paramsFromProvider(parametersAnnotation.source());
        return params;
    }

    private Object[] paramsFromProvider(Class sourceClass) {
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

    private boolean isParameterised(FrameworkMethod method) {
        return method.getMethod().isAnnotationPresent(Parameters.class);
    }

    @Override
    protected java.util.List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();

        addTestMethods(getTestClass().getAnnotatedMethods(Test.class), resultMethods, false);

        return resultMethods;
    }

    private void addTestMethods(List<FrameworkMethod> scenarios, List<FrameworkMethod> resultMethods, boolean flat) {
        paramSetIndexMap = new HashMap<FrameworkMethod, Integer>();
        for (FrameworkMethod scenarioMethod : scenarios) {
            if (isParameterised(scenarioMethod) && !flat)
                addScenarioForEachParamSet(resultMethods, scenarioMethod);
            else
                addScenarioOnce(resultMethods, scenarioMethod);
        }
    }

    private void addScenarioForEachParamSet(List<FrameworkMethod> resultMethods, FrameworkMethod scenarioMethod) {
        int paramSetSize = paramsFromAnnotation(scenarioMethod).length;

        for (int i = 0; i < paramSetSize; i++)
            addScenarioOnce(resultMethods, scenarioMethod);

        paramSetIndexMap.put(scenarioMethod, 0);
    }

    private void addScenarioOnce(List<FrameworkMethod> resultMethods, FrameworkMethod scenarioMethod) {
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
        Description description = Description.createSuiteDescription(getName(),
                getTestClass().getAnnotations());

        List<FrameworkMethod> resultMethods = new ArrayList<FrameworkMethod>();
        addTestMethods(getTestClass().getAnnotatedMethods(Test.class), resultMethods, true);

        for (FrameworkMethod child : resultMethods) {
            if (isParameterised(child)) {
                Description parametrised = Description.createSuiteDescription(testName(child));

                Object[] params = paramsFromAnnotation(child);

                for (Object paramSet : params)
                    parametrised.addChild(Description.createTestDescription(getTestClass().getJavaClass(),
                            Utils.stringify(paramSet) + " (" + testName(child) + ")", child.getAnnotations()));

                description.addChild(parametrised);
            } else {
                description.addChild(describeChild(child));
            }
        }
        return description;
    }
}
