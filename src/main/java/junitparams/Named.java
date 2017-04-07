package junitparams;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation for test parameter providers.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Named {
	/**
	 * Name of the set of parameters provided by a method.
	 * <p>
	 * On a test case, use the @see Parameters annotation with the
	 * option "named" to link to use the corresponding parameters.
	 * <p>
	 * Example: <code>@Named("exemplaryPeople")</code>
	 * <p>
	 * Corresponding test case annotation example:
	 * <p>
	 * <code>@Parameters(named = "exemplaryPeople")</code>
	 */
	String value() default "";
}
