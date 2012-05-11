package junitparams;

import java.lang.annotation.*;

import junitparams.internal.*;

/**
 * 
 * Denotes that parameters for a annotated test method should be taken from a
 * file.
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
     * The mapper which knows how to parse the file and turn it into a valid set
     * of parameters. By default it is an IdentityMapper, meaning the file has
     * exactly the same format as the @Parameters({}) annotation.
     */
    Class<? extends DataMapper> mapper() default IdentityMapper.class;

}
