package junitparams.converters;

import java.lang.annotation.Annotation;

public interface Converter<A extends Annotation, T> {

    void initialize(A annotation);

    T convert(Object object) throws ConversionFailedException;
}
