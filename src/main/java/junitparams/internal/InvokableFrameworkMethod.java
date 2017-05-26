package junitparams.internal;

import java.lang.reflect.Method;
import junitparams.JUnitParamsRunner;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Base for {@link FrameworkMethod} classes that provide a {@link Statement} for invoking.
 */
public abstract class InvokableFrameworkMethod extends DescribableFrameworkMethod {

    InvokableFrameworkMethod(Method method) {
        super(method);
    }

    /**
     * Create a {@link Statement} that when called will invoke the method.
     *
     * <p>This is usually called from the
     * {@link JUnitParamsRunner#methodInvoker(FrameworkMethod, Object)} method via the
     * {@link MethodBlockSupplier} which is usually called from within the
     * {@link #run(MethodBlockSupplier, RunNotifier)} method.
     *
     * @param test
     *         the object on which the method will be invoked.
     * @return the {@link Statement}.
     */
    public abstract Statement getInvokeStatement(Object test);

    void runMethodInvoker(RunNotifier notifier, Statement methodInvoker,
            Description methodWithParams) {
        EachTestNotifier eachNotifier = new EachTestNotifier(notifier, methodWithParams);
        eachNotifier.fireTestStarted();
        try {
            methodInvoker.evaluate();
        } catch (AssumptionViolatedException e) {
            eachNotifier.addFailedAssumption(e);
        } catch (Throwable e) {
            eachNotifier.addFailure(e);
        } finally {
            eachNotifier.fireTestFinished();
        }
    }

    public abstract void run(MethodBlockSupplier supplier, RunNotifier notifier);
}
