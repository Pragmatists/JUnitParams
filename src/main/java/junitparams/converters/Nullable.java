
package junitparams.converters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows use null string values in parametrized test 
 * 
 * <pre>
 * @Test
 * @Parameters({" null "})
 * public void shouldConvertToNullIgnoringWhitespaces(@Nullable String value) {
 *   assertThat(value).isNull();
 * }
 * </pre>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Param(converter = NullableConverter.class)
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
public @interface Nullable {
    
    String nullIdentifier() default "null";
}
