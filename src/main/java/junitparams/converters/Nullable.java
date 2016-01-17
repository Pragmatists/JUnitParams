
package junitparams.converters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import junitparams.Parameters;

/**
 * Allows test null values defined as a String array in {@link Parameters} 
 * 
 * @author Peter Jurkovic
 * 
 * <p>
 * Example: 
 * <pre>
 * {@literal @}Test
 * {@literal @}Parameters({" null "})
 * public void shouldBeNull({@literal @}Nullable String value) {
 *     assertThat(value).isNull();
 * }
 * </pre>
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Param(converter = NullableConverter.class)
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
public @interface Nullable {
    
    /**
     * Defines parameter value which will be replaced by Java null
     */
    String nullIdentifier() default "null";
}
