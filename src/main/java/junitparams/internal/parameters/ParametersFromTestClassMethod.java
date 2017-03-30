package junitparams.internal.parameters;


import org.junit.runners.model.FrameworkMethod;

import junitparams.NullType;
import junitparams.Parameters;

class ParametersFromTestClassMethod extends ParamsFromMethodCommon {

    private final Class<?> testClass;
    private final Parameters annotation;

    ParametersFromTestClassMethod(Class<?> testClass, FrameworkMethod frameworkMethod, Parameters annotation,
                                  ResultAdapter resultAdapter) {
        super(frameworkMethod, testClass, annotation, resultAdapter);
        this.testClass = testClass;
        this.annotation = annotation;
    }

    @Override
    public boolean isApplicable() {
        return annotation != null
                && annotation.source().isAssignableFrom(NullType.class)
                && (!annotation.method().isEmpty() || canUseDefaultProvidingMethod());
    }

    /**
     * Determine if can use a default providing method based on the test method name.
     * <p>
     * Note that this should only match when no arguments are given, i.e. {@code @Parameters}.
     * <p>
     * It does not make sense for this to match when explicit parameter values are provided so we do not match when
     * the value array is not empty for example {@code @Parameters({"a", "b"}).
     *
     * @return true if it is possible to use the default providing method
     */
    private boolean canUseDefaultProvidingMethod() {
        return annotation.value().length == 0 && containsDefaultParametersProvidingMethod(testClass);
    }
}
