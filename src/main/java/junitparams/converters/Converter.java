package junitparams.converters;

import java.lang.annotation.Annotation;

/**
 * Defines the logic to convert parameter annotated with A to type T. Converter must have a public no-args constructor. Configuration is
 * done via {@link Converter#initialize(java.lang.annotation.Annotation)} method<br>
 * Inspired by javax.validation.ConstraintValidator
 *
 * @param <A> type of annotation mentioning this converter
 * @param <T> conversion target type
 */
public interface Converter<A extends Annotation, T> {

    /**
     * Initializes this converter - you can read your annotation config here.
     */
    void initialize(A annotation);

    /**
     * Converts param to desired type.
     *
     * @throws ConversionFailedException
     */
    T convert(Object param) throws ConversionFailedException;
}
