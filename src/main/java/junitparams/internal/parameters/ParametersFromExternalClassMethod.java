package junitparams.internal.parameters;

import org.junit.runners.model.FrameworkMethod;

import junitparams.NullType;
import junitparams.Parameters;

class ParametersFromExternalClassMethod implements ParametrizationStrategy {
    private ParamsFromMethodCommon paramsFromMethodCommon;
    private Parameters annotation;

    ParametersFromExternalClassMethod(FrameworkMethod frameworkMethod) {
        this.paramsFromMethodCommon = new ParamsFromMethodCommon(frameworkMethod);
        annotation = frameworkMethod.getAnnotation(Parameters.class);
    }

    @Override
    public Object[] getParameters() {
        Class<?> sourceClass = annotation.source();
        return !annotation.method().isEmpty()
            ? paramsFromMethodCommon.paramsFromMethod(sourceClass)
            : paramsFromMethodCommon.paramsFromNamedMethod(sourceClass);
    }

    @Override
    public boolean isApplicable() {
        return annotation != null
                && !annotation.source().isAssignableFrom(NullType.class)
                && (!annotation.method().isEmpty() || ! annotation.named().isEmpty());
    }
}