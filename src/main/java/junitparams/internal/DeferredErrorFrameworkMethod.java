package junitparams.internal;

import java.lang.reflect.Method;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.Statement;

/**
 * Encapsulates a {@link Throwable} that was caught during initialization so that it can be
 * thrown during execution in order to preserve previous behavior.
 */
public class DeferredErrorFrameworkMethod extends InvokableFrameworkMethod {

    private final Description description;
    private final Throwable throwable;

    DeferredErrorFrameworkMethod(Method method, Description description, Throwable throwable) {
        super(method);
        this.description = description;
        this.throwable = throwable;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public Statement getInvokeStatement(Object test) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                throw throwable;
            }
        };
    }

    @Override
    public void run(MethodBlockSupplier supplier, RunNotifier notifier) {
        // Do not call the MethodBlockSupplier as that could introduce additional errors, simply
        // throw the encapsulated Throwable immediately.
        runMethodInvoker(notifier, getInvokeStatement(notifier), getDescription());
    }
}
