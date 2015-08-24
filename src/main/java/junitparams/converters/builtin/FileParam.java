package junitparams.converters.builtin;

import junitparams.converters.ConvertParam;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@ConvertParam(value = FileConverter.class)
public @interface FileParam {

	// Stereotype definition

}
