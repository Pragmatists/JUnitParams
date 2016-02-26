package junitparams.custom;

import java.lang.annotation.Annotation;

public interface ParametersProvider<A extends Annotation> {

    void initialize(A parametersAnnotation);

    Object[] provideParameters();
}
