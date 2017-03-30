package junitparams.internal.parameters;

import junitparams.Parameters;
import org.junit.runners.model.FrameworkMethod;

class ParametersFromValue implements ParametrizationStrategy {

    private final Parameters parametersAnnotation;

    ParametersFromValue(FrameworkMethod frameworkMethod) {
        this(frameworkMethod.getAnnotation(Parameters.class));
    }

    ParametersFromValue(Parameters annotation) {
        parametersAnnotation = annotation;
    }

    @Override
    public Object[] getParameters() {
        return parametersAnnotation.value();
    }

    @Override
    public boolean isApplicable() {
        return parametersAnnotation != null && parametersAnnotation.value().length > 0;
    }
}