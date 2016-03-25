package junitparams.internal.annotation;

import java.lang.annotation.Annotation;

import org.junit.runners.model.FrameworkMethod;

import junitparams.Parameters;
import junitparams.custom.CustomParameters;

public class FrameworkMethodAnnotations {

    private final FrameworkMethod frameworkMethod;

    public FrameworkMethodAnnotations(FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;
    }

    public boolean isParametrised() {
        return hasAnnotation(Parameters.class)
                || hasCustomParameters();
    }

    public Annotation[] allAnnotations() {
        return frameworkMethod.getAnnotations();
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        return frameworkMethod.getAnnotation(annotationType);
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotation) {
        return getAnnotation(annotation) != null;
    }

    public boolean hasCustomParameters() {
        return getCustomParameters() != null;
    }

    public CustomParametersDescriptor getCustomParameters() {
        CustomParameters customParameters = frameworkMethod.getAnnotation(CustomParameters.class);
        if (customParameters != null) {
            return new CustomParametersDescriptor(customParameters);
        }

        for (Annotation annotation : frameworkMethod.getAnnotations()) {
            customParameters = annotation.annotationType().getAnnotation(CustomParameters.class);
            if (customParameters != null) {
                return new CustomParametersDescriptor(customParameters, annotation);
            }
        }
        return null;
    }
}
