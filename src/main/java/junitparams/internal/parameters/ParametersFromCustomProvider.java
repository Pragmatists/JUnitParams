package junitparams.internal.parameters;

import org.junit.runners.model.FrameworkMethod;

import junitparams.custom.ParametersProvider;
import junitparams.internal.annotation.CustomParametersDescriptor;
import junitparams.internal.annotation.FrameworkMethodAnnotations;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class ParametersFromCustomProvider implements ParametrizationStrategy {

    private final FrameworkMethodAnnotations frameworkMethodAnnotations;
    private final FrameworkMethod frameworkMethod;

    ParametersFromCustomProvider(FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;
        frameworkMethodAnnotations = new FrameworkMethodAnnotations(frameworkMethod);
    }

    @Override
    public boolean isApplicable() {
        return frameworkMethodAnnotations.hasCustomParameters();
    }

    @Override
    public Object[] getParameters() {
        CustomParametersDescriptor parameters = frameworkMethodAnnotations.getCustomParameters();
        ParametersProvider provider = instantiate(parameters.provider());
        provider.initialize(parameters.annotation(), frameworkMethod);
        return provider.getParameters();
    }

    private ParametersProvider instantiate(Class<? extends ParametersProvider> providerClass) {
        try {
            return providerClass.getConstructor().newInstance();
        } catch (Exception ignored) {
            throw new RuntimeException("Your Provider class must have a public no-arg constructor!");
        }
    }

}
