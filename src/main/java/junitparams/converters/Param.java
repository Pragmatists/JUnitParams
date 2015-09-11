package junitparams.converters;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates parametrized test parameter with information about {@link Converter} that should be used for parameter conversion.
 * <p>
 * Can also be used to create custom annotations.<br>
 * example:
 * <pre>
 *  &#064;Retention(RetentionPolicy.RUNTIME)
 *  &#064;Target(ElementType.PARAMETER)
 *  &#064;Param(converter = FormattedDateConverter.class)
 *  public &#064;interface DateParam {
 *
 *      String format() default "dd.MM.yyyy";
 *  }
 * </pre>
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
public @interface Param {

    Class<? extends Converter> converter();
}
