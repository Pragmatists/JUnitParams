package junitparams.custom;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Tells JUnitParams which {@link ParametersProvider} to use for parameters generation.<br>
 * Use instead of {@link junitparams.Parameters} annotation.
 * <p>
 * Can also be used to create custom annotations.<br>
 * Check {@link junitparams.FileParameters}, {@link FileParametersProvider} and CustomParametersProviderTest for usage examples.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface CustomParameters {

    /**
     * @return Your custom parameters provider class.
     */
    Class<? extends ParametersProvider> provider();

}
