package junitparams.custom;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@CustomParameters(provider = CombinedParametersProvider.class)
public @interface CombinedParameters {
    /**
     * Parameter values defined as a String array.
     * Each of the elements is a list of values that should be tested for parameters.
     * Using this annotation will result in creating a n-fold cartesian product of parameter values effectively testing
     * each possible combination.
     * Values in the array must match the test method's parameters in order and type.
     * Whitespace characters are trimmed (use source class or method if You need to provide such parameters)
     * <p>
     * Example:<code>@CombinedParameters({"han,chewie","33,204"})
     *     public void shouldTestAllNameAgeCombinations(String name, Integer age)
     * </code>
     *
     */
    String[] value() default {};
}
