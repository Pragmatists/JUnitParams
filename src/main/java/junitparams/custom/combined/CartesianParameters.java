package junitparams.custom.combined;

import junitparams.Parameters;
import junitparams.custom.CustomParameters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A cartesian based parameter set that reuses the {@link Parameters} functionality as a source of parameter sets.
 */
@CustomParameters(provider = CartesianParametersProvider.class)
@Retention(RUNTIME)
@Target(METHOD)
@SuppressWarnings("WeakerAccess")
public @interface CartesianParameters {
    /**
     * A set of {@link Parameters} annotations.
     *
     * @return the annotations
     */
    Parameters[] value();
}
