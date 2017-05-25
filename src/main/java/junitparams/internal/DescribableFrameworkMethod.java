package junitparams.internal;

import java.lang.reflect.Method;

import org.junit.runner.Describable;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;

/**
 * A {@link FrameworkMethod} that also provides a {@link Description}.
 */
public abstract class DescribableFrameworkMethod extends FrameworkMethod implements Describable {

    DescribableFrameworkMethod(Method method) {
        super(method);
    }
}
