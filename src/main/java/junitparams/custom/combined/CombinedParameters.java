package junitparams.custom.combined;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import junitparams.custom.CustomParameters;

@Retention(RetentionPolicy.RUNTIME)
@CustomParameters(provider = CombinedParametersProvider.class)
public @interface CombinedParameters {
    /**
     * Parameter values defined as a String array.
     * Each of the elements is a list of values that should be tested for parameters.
     * Using this annotation will result in creating a n-fold cartesian product of parameter values effectively testing
     * each possible combination.
     * Values in the array must match the test method's parameters in order and type.
     * <p>
     * Example:<br>
     * <code>@CombinedParameters({"han,chewie","33,204"})<br>
     * public void shouldTestAllNameAgeCombinations(String name, Integer age)
     * </code>
     */
    String[] value() default {};
}
