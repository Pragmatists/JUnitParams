package junitparams.internal;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.Filterable;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * A {@link FrameworkMethod} that represents a parameterized method.
 *
 * <p>This contains a list of {@link InstanceFrameworkMethod} that represent the individual
 * instances of this method, one per parameter set.
 */
public class ParameterisedFrameworkMethod extends InvokableFrameworkMethod implements Filterable {

    /**
     * The base description, used as a template when creating {@link Description}.
     */
    private final Description baseDescription;

    /**
     * The list of {@link InstanceFrameworkMethod} that represent individual instances of this
     * method.
     */
    private List<InstanceFrameworkMethod> instanceMethods;

    /**
     * The {@link Description}, created lazily and updated after filtering.
     */
    private Description description;

    ParameterisedFrameworkMethod(Method method, Description baseDescription,
            List<InstanceFrameworkMethod> instanceMethods) {
        super(method);
        this.baseDescription = baseDescription;
        this.instanceMethods = instanceMethods;
    }

    @Override
    public Description getDescription() {
        if (description == null) {
            description = baseDescription.childlessCopy();
            for (InstanceFrameworkMethod instanceMethod : instanceMethods) {
                description.addChild(instanceMethod.getInstanceDescription());
            }
        }

        return description;
    }

    public List<InstanceFrameworkMethod> getMethods() {
        return instanceMethods;
    }

    @Override
    public void filter(Filter filter) throws NoTestsRemainException {
        int count = instanceMethods.size();
        for (Iterator<InstanceFrameworkMethod> i = instanceMethods.iterator(); i.hasNext(); ) {
            InstanceFrameworkMethod instanceMethod = i.next();
            if (filter.shouldRun(instanceMethod.getInstanceDescription())) {
                try {
                    filter.apply(instanceMethod);
                } catch (NoTestsRemainException e) {
                    i.remove();
                }
            } else {
                i.remove();
            }
        }

        if (instanceMethods.size() != count) {
            // Some instance methods have been filtered out, so invalidate the description.
            description = null;
        }

        if (instanceMethods.isEmpty()) {
            throw new NoTestsRemainException();
        }
    }

    @Override
    public Statement getInvokeStatement(Object test) {
        throw new IllegalStateException("Should never be called");
    }

    @Override
    public void run(MethodBlockSupplier supplier, RunNotifier notifier) {
        for (InstanceFrameworkMethod frameworkMethod : instanceMethods) {
            frameworkMethod.run(supplier, notifier);
        }
    }
}
