package junitparams.internal;

import java.lang.reflect.Method;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A {@link FrameworkMethod} that represents an instance of an
 * {@link ParameterisedFrameworkMethod}, that is the combination of the test method with the
 * parameter set that it will be passed.
 */
public class InstanceFrameworkMethod extends InvokableFrameworkMethod {

    private final Description description;

    private final Description instanceDescription;

    private final Object parametersSet;

    /**
     * Create an {@link InstanceFrameworkMethod}.
     *
     * <p>It has two {@link Description} instances because it has to provide different
     * {@link Description} to {@link TestRule} instances than other usages in order to maintain
     * backwards compatibility.
     *
     * @param method the test method
     * @param description the description that is supplied to {@link TestRule} instances.
     * @param instanceDescription the description used for all other purposes, e.g. filtering,
     *         {@link Runner#getDescription()} and {@link RunListener}.
     * @param parametersSet the set of parameters to pass to the method.
     */
    InstanceFrameworkMethod(Method method, Description description,
            Description instanceDescription, Object parametersSet) {
        super(method);
        this.description = description;
        this.instanceDescription = instanceDescription;
        this.parametersSet = parametersSet;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    Description getInstanceDescription() {
        return instanceDescription;
    }

    @Override
    public Statement getInvokeStatement(Object test) {
        return new InvokeParameterisedMethod(this, test, parametersSet);
    }

    @Override
    public void run(MethodBlockSupplier supplier, RunNotifier notifier) {
        runMethodInvoker(notifier, supplier.getMethodBlock(this), getInstanceDescription());
    }
}
