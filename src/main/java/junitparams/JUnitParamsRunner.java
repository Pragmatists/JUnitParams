package junitparams;

import java.util.*;

import org.junit.*;
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

    private ParameterisedTestClassRunner parameterisedRunner = new ParameterisedTestClassRunner();

    public JUnitParamsRunner(Class<?> klass) throws InitializationError {
        super(klass);
        computeTestMethods();
    }

    protected void collectInitializationErrors(List<Throwable> errors) {
    }

    @Override
    protected void runChild(FrameworkMethod method, RunNotifier notifier) {
        if (method.getAnnotation(Ignore.class) != null) {
            Description ignoredMethod = parameterisedRunner.describeParameterisedMethod(method);
            for (Description child : ignoredMethod.getChildren()) {
                notifier.fireTestIgnored(child);
            }
            return;
        }

        if (!parameterisedRunner.runParameterisedTest(method, methodBlock(method), notifier))
            super.runChild(method, notifier);
    }

    @Override
    protected List<FrameworkMethod> computeTestMethods() {
        return parameterisedRunner.computeTestMethods(getTestClass().getAnnotatedMethods(Test.class), false);
    }

    @Override
    protected Statement methodInvoker(FrameworkMethod method, Object test) {
        Statement methodInvoker = parameterisedRunner.parameterisedMethodInvoker(method, test);
        if (methodInvoker == null)
            methodInvoker = super.methodInvoker(method, test);

        return methodInvoker;
    }

    @Override
    public Description getDescription() {
        Description description = Description.createSuiteDescription(getName(), getTestClass().getAnnotations());
        List<FrameworkMethod> resultMethods = parameterisedRunner.computeTestMethods(getTestClass().getAnnotatedMethods(Test.class), true);

        for (FrameworkMethod method : resultMethods)
            description.addChild(describeMethod(method));

        return description;
    }

    private Description describeMethod(FrameworkMethod method) {
        Description child = parameterisedRunner.describeParameterisedMethod(method);

        if (child == null)
            child = describeChild(method);

        return child;
    }

    public static Object[] $(Object... params) {
        return params;
    }
}
