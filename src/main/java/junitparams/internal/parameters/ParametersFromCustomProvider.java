package junitparams.internal.parameters;

import org.junit.runners.model.FrameworkMethod;

import junitparams.custom.ParametersProvider;
import junitparams.internal.annotation.CustomParametersDescriptor;
import junitparams.internal.annotation.FrameworkMethodAnnotations;

public class ParametersFromCustomProvider implements ParametrizationStrategy {
    private final FrameworkMethodAnnotations frameworkMethodAnnotations;

    public ParametersFromCustomProvider(FrameworkMethod frameworkMethod) {
        frameworkMethodAnnotations = new FrameworkMethodAnnotations(frameworkMethod);
    }

    @Override
    public boolean isApplicable() {
        return frameworkMethodAnnotations.hasCustomParameters();
    }

    @Override
    public Object[] getParameters() {
        CustomParametersDescriptor parameters = frameworkMethodAnnotations.getCustomParameters();
        ParametersProvider provider = instantiateParametersProvider(parameters.providerClass());
        provider.initialize(parameters.annotation());
        return provider.provideParameters();
    }

    private ParametersProvider instantiateParametersProvider(Class<? extends ParametersProvider> providerClass) {
        try {
            return providerClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Your Provider class must have a public no-arg constructor!", e);
        }
    }

}
