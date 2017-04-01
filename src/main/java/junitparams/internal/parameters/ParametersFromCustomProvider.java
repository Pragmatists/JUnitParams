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
        this.frameworkMethod=frameworkMethod;
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
        provider.initialize(parameters.annotation());
        return provider.getParameters();
    }

    private ParametersProvider instantiate(Class<? extends ParametersProvider> providerClass) {

        try {
            Constructor<? extends ParametersProvider> constructor = providerClass.getConstructor(FrameworkMethod.class);
            return newParametersProviderInstance(constructor, frameworkMethod);
        } catch (NoSuchMethodException ignored) {
            // no framework constructor method
        }

        try {
            Constructor<? extends ParametersProvider> constructor = providerClass.getConstructor();
            return newParametersProviderInstance(constructor);
        } catch (NoSuchMethodException ignored) {
            throw new RuntimeException("Your Provider class must have a public no-arg constructor!");
        }
    }

    private ParametersProvider newParametersProviderInstance(Constructor<? extends ParametersProvider> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to initialise custom provider", e.getCause());
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke custom provider constructor " + constructor);
        }
    }
}
