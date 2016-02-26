package junitparams.internal.annotation;

import java.lang.annotation.Annotation;

import junitparams.custom.CustomParameters;
import junitparams.custom.ParametersProvider;

public class CustomParametersDescriptor {
    private final CustomParameters customParameters;
    private final Annotation customAnnotation;

    public CustomParametersDescriptor(CustomParameters customParameters) {
        this(customParameters, customParameters);
    }

    public CustomParametersDescriptor(CustomParameters customParameters, Annotation customAnnotation) {
        this.customParameters = customParameters;
        this.customAnnotation = customAnnotation;
    }

    public Class<? extends ParametersProvider> providerClass() {
        return customParameters.provider();
    }

    public Annotation annotation() {
        return customAnnotation;
    }
}
