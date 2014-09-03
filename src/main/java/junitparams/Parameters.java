package junitparams;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.lang.model.type.NullType;

/**
 * THE annotation for the test parameters. Use it to say that a method takes
 * some parameters and define how to obtain them.
 * 
 * @author Pawel Lipinski
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Parameters {
    /**
     * Parameter values defined as a String array. Each element in the array is
     * a full parameter set, comma-separated. The values must match the method
     * parameters in order and type.
     * Example: <code>@Parameters({
     *                    "1, joe, 26.4, true", 
     *                    "2, angie, 37.2, false"}</code>
     */
    String[] value() default {};

    /**
     * Parameter values defined externally. The specified class must have at
     * least one public static method starting with <code>provide</code>
     * returning <code>Object[]</code>. All such methods are used, so you can
     * group your examples. The resulting array should contain parameter sets in
     * its elements. Each parameter set must be another Object[] array, which
     * contains parameter values in its elements.
     * Example: <code>@Parameters(source = PeopleProvider.class)</code>
     */
    Class<?> source() default NullType.class;

    /**
     * Parameter values returned by a method within the test class. This way you
     * don't need additional classes and the test code may be a bit cleaner. The
     * format of the data returned by the method is the same as for the source
     * annotation class.
     * Example: <code>@Parameters(method = "examplaryPeople")</code>
     * 
     * You can use multiple methods to provide parameters - use comma to do it:
     * Example: <code>@Parameters(method = "womenParams, menParams")</code>
     * 
     * You can provide parameters to the method using the attribute
     * {@link #methodParams()}
     */
    String method() default "";

    /**
     * Parameter values for the method used to generate parameter values for the
     * test method.
     * Example: <code>@Parameters(method = "getTestCase",
     * 							  methodParams = {"case1", "case2"})</code>
     * 
     * Note that this will be applied to every method specified in method()
     * Example: <code>@Parameters(method = "womenParams, menParams",
     * 							  methodParams = {"case1", "case2"})</code>
     * 
     * If <code>methodParams</code> attribute is provided, the method must have
     * an <code>Object[]</code> as the sole argument.
     */
    String[] methodParams() default {};
}
