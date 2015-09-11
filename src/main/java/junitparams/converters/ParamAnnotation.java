package junitparams.converters;

import java.lang.annotation.Annotation;

public class ParamAnnotation {

    public static boolean matches(Annotation annotation) {
        return getParam(annotation) != null;
    }

    public static Object convert(Annotation annotation, Object param) throws ConversionFailedException {
        return converter(annotation).convert(param);
    }

    private static Param getParam(Annotation annotation) {
        if (annotation.annotationType().isAssignableFrom(Param.class)) {
            return (Param) annotation;
        }
        return annotation.annotationType().getAnnotation(Param.class);
    }

    private static Converter converter(Annotation annotation) {
        Converter converter = null;
        try {
            converter = getParam(annotation).converter().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Your Converter class must have a public no-arg constructor!", e);
        }
        converter.initialize(annotation);
        return converter;
    }
}
