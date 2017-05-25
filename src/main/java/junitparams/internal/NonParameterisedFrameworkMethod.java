package junitparams.internal;

import java.lang.reflect.Method;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A {@link FrameworkMethod} that represents an unparameterized method.
 */
public class NonParameterisedFrameworkMethod
        extends InvokableFrameworkMethod {

    private final Description description;

    private final boolean ignored;

    /**
     * Create a non-parameterised method.
     *
     * @param method the test method
     * @param description the description of the method
     * @param ignored true if the method should be ignore, either because the method has the
     *     {@link Ignore} annotation, or because it is parameterised but is given no parameters.
     */
    NonParameterisedFrameworkMethod(Method method, Description description, boolean ignored) {
        super(method);
        this.description = description;
        this.ignored = ignored;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public Statement getInvokeStatement(Object test) {
        return new InvokeNonParameterisedMethod(this, test);
    }

    @Override
    public void run(MethodBlockSupplier supplier, RunNotifier notifier) {
        runMethodInvoker(notifier, supplier.getMethodBlock(this), getDescription());
    }

    public boolean isIgnored() {
        return ignored;
    }
}
