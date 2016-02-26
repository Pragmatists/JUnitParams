package junitparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import junitparams.custom.CustomParameters;
import junitparams.custom.FileParametersProvider;
import junitparams.mappers.DataMapper;
import junitparams.mappers.IdentityMapper;

/**
 * Denotes that parameters for a annotated test method should be taken from an
 * external resource.
 *
 * @author Pawel Lipinski
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@CustomParameters(provider = FileParametersProvider.class)
public @interface FileParameters {

    /**
     * File name (with full path) of the file with data.
     */
    String value();

    /**
     * The mapper which knows how to get the data from the external resource and
     * turn it into a valid set of parameters. By default it is an
     * IdentityMapper, meaning the resource has exactly the same format as the
     * <p/>
     * &#064;Parameters annotation value (when passed as String), being CSV.
     */
    Class<? extends DataMapper> mapper() default IdentityMapper.class;

    /**
     * Encoding to use when reading file contents.
     */
    String encoding() default "UTF-8";

}
