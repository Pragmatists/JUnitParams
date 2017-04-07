package junitparams.internal.parameters;

import junitparams.NullType;
import junitparams.Parameters;
import org.junit.runners.model.FrameworkMethod;

class ParametersFromNamedParametersMethod implements ParametrizationStrategy {
    private final ParamsFromMethodCommon paramsFromMethodCommon;
    private final Class<?> testClass;
    private final Parameters annotation;

    ParametersFromNamedParametersMethod(FrameworkMethod frameworkMethod, Class<?> testClass) {
        this.testClass = testClass;
        paramsFromMethodCommon = new ParamsFromMethodCommon(frameworkMethod);
        annotation = frameworkMethod.getAnnotation(Parameters.class);
    }

    @Override
    public Object[] getParameters() {
        return paramsFromMethodCommon.paramsFromNamedMethod(testClass);
    }

    @Override
    public boolean isApplicable() {
        return annotation != null
                && annotation.source().isAssignableFrom(NullType.class)
                && annotation.method().isEmpty()
                && !annotation.named().isEmpty();
    }
}
