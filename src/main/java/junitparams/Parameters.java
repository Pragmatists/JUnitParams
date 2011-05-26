package junitparams;

import java.lang.annotation.*;

import javax.lang.model.type.*;

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
     * Example: <code>@Paramers({
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
     * Example: <code>@Paramers(source = PeopleProvider.class)
     */
    Class<?> source() default NullType.class;

    /**
     * Parameter values returned by a method within the test class. This way you
     * don't need additional classes and the test code may be a bit cleaner. The
     * format of the data returned by the method is the same as for the source
     * annotation class.
     * Example: <code>@Paramers(method = "examplaryPeople")</code>
     */
    String method() default "";
}
