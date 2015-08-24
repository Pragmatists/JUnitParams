package junitparams.converters.builtin;

import junitparams.converters.ConvertParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@ConvertParam(value = URIConverter.class)
public @interface URIParam {

	// Stereotype definition

}
