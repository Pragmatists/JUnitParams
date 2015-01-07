package junitparams.naming;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation to specify the name for individual test case.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TestCaseName {

    /**
     * A template of the individual test case name.
     * This template can contain macros, which will be substituted by their actual values at runtime.
     * <p>
     * Supported macros are:
     * <ul>
     * <li><b>{index}</b> - index of parameters set (starts from zero). Hint: use it to avoid names duplication.</li>
     * <li><b>{params}</b> - parameters set joined by comma.</li>
     * <li><b>{method}</b> - testing method name.</li>
     * <li>
     *     <b>{0}</b>, <b>{1}</b>, <b>{2}</b> - single parameter by index in current parameters set.
     *     If there is no parameter with such index, it will use empty string.
     * </li>
     * </ul>
     * Lets assume, that we are testing Fibonacci sequence generator. We have a test with the following signature
     * <pre><code>
     * {@literal @}Parameters({ "0,1", "8,34" })
     * public void testFibonacci(int indexInSequence, int expectedNumber) { ... }
     * </code></pre>
     * Here are some examples, that can be used as a test name template:
     * <ul>
     * <li>{method}({params}) => testFibonacci(0, 1), testFibonacci(8, 34)</li>
     * <li>fibonacci({0}) = {1} => fibonacci(0) = 1, fibonacci(8) = 34</li>
     * <li>{0} element should be {1} => 0 element should be 1, 8 element should be 34</li>
     * <li>Fibonacci sequence test #{index} => Fibonacci sequence test #0, Fibonacci sequence test #1</li>
     * </ul>
     */
    String value() default MacroSubstitutionNamingStrategy.DEFAULT_TEMPLATE;
}