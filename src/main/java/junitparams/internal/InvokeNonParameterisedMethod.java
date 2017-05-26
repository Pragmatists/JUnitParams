package junitparams.internal;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * JUnit invoker for non-parameterised test methods
 */
public class InvokeNonParameterisedMethod extends Statement {

    private final FrameworkMethod testMethod;
    private final Object testClass;

    InvokeNonParameterisedMethod(FrameworkMethod testMethod, Object testClass) {
        this.testMethod = testMethod;
        this.testClass = testClass;
    }

    @Override
    public void evaluate() throws Throwable {
        testMethod.invokeExplosively(testClass);
    }

}
