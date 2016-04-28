package junitparams.internal.parameters;

import javax.lang.model.type.NullType;

import org.junit.runners.model.FrameworkMethod;

import junitparams.Parameters;

class ParametersFromTestClassMethod implements ParametrizationStrategy {
    private final ParamsFromMethodCommon paramsFromMethodCommon;
    private final Class<?> testClass;
    private final Parameters annotation;

    ParametersFromTestClassMethod(FrameworkMethod frameworkMethod, Class<?> testClass) {
        this.testClass = testClass;
        paramsFromMethodCommon = new ParamsFromMethodCommon(frameworkMethod);
        annotation = frameworkMethod.getAnnotation(Parameters.class);
    }

    @Override
    public Object[] getParameters() {
        return paramsFromMethodCommon.paramsFromMethod(testClass);
    }

    @Override
    public boolean isApplicable() {
        return annotation != null
               && annotation.source().isAssignableFrom(NullType.class)
               && (!annotation.method().isEmpty() || paramsFromMethodCommon.containsDefaultParametersProvidingMethod(testClass));
    }
}
