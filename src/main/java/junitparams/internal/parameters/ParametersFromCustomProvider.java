package junitparams.internal.parameters;

import org.junit.runners.model.FrameworkMethod;

import junitparams.custom.ParametersProvider;
import junitparams.internal.annotation.CustomParametersDescriptor;
import junitparams.internal.annotation.FrameworkMethodAnnotations;

import java.lang.reflect.Constructor;

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
        Constructor<?>[] constructors = providerClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            if(parameterTypes.length==1){
                if(parameterTypes[0] == FrameworkMethod.class){
                    try {
                        return (ParametersProvider) constructor.newInstance(frameworkMethod);
                    } catch (Exception e) {
                        throw new RuntimeException("init error", e);
                    }
                }else{
                    throw new RuntimeException("init error,1-arg constructor parameter type must be FrameworkMethod!");
                }
            }else {
                try {
                    return (ParametersProvider) constructor.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Your Provider class must have a public no-arg constructor!", e);
                }
            }
        }
        throw new RuntimeException("Your Provider class must have a public no-arg constructor!");
    }

}
