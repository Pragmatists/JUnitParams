package junitparams.internal.parameters;

import junitparams.Parameters;
import org.junit.runners.model.FrameworkMethod;

class ParametersFromValue implements ParametrizationStrategy {

    private final Parameters parametersAnnotation;

    ParametersFromValue(FrameworkMethod frameworkMethod) {
        parametersAnnotation = frameworkMethod.getAnnotation(Parameters.class);
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