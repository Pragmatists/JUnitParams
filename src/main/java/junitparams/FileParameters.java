package junitparams;

import java.lang.annotation.*;

import junitparams.mappers.*;

/**
 * 
 * Denotes that parameters for a annotated test method should be taken from an
 * external resource.
 * 
 * @author Pawel Lipinski
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FileParameters {

    /**
     * File name (with full path) of the file with data.
     */
    String value();

    /**
     * The mapper which knows how to get the data from the external resource and
     * turn it into a valid set of parameters. By default it is an
     * IdentityMapper, meaning the resource has exactly the same format as the
     * 
     * &#064;Parameters annotation value (when passed as String), being CSV.
     */
    Class<? extends DataMapper> mapper() default IdentityMapper.class;

}
