package junitparams.internal.annotation;

import java.lang.annotation.Annotation;

import junitparams.custom.CustomParameters;
import junitparams.custom.ParametersProvider;

public class CustomParametersDescriptor {

    private final Annotation customAnnotation;

    private final Class<? extends ParametersProvider> provider;

    public CustomParametersDescriptor(CustomParameters customParameters) {
        this(customParameters, customParameters);
    }

    public CustomParametersDescriptor(CustomParameters customParameters, Annotation customAnnotation) {
        this.provider = customParameters.provider();
        this.customAnnotation = customAnnotation;
    }

    public Class<? extends ParametersProvider> provider() {
        return provider;
    }

    public Annotation annotation() {
        return customAnnotation;
    }
}
