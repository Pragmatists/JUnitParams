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
@Deprecated
public @interface ConvertParam {

    Class<? extends ParamConverter<?>> value();

    /**
     * Options / settings to be used by the converter class
     * @return
     */
    String options() default "";

}
