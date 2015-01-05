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
     */
    String value() default MacroSubstitutionNamingStrategy.DEFAULT_TEMPLATE;
}