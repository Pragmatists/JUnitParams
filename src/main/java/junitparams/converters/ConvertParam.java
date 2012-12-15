package junitparams.converters;

import java.lang.annotation.*;

/**
 * 
 * Defines a converter which should be used to convert a parameter to expected
 * type.
 * 
 * @author Pawel Lipinski
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertParam {

    Class<? extends ParamConverter<?>> value();

    String options() default "";

}
