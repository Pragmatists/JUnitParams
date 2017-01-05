package junitparams.converters;

public class NullableConverter implements Converter<Nullable, Object> {
    
    private String nullIdentifier;
    
    public void initialize(Nullable annotation) {
        nullIdentifier = annotation.nullIdentifier() == null ? "null" : annotation.nullIdentifier();
    }

    public Object convert(Object param) throws ConversionFailedException {
        if (param instanceof String && ((String) param).equalsIgnoreCase(nullIdentifier)) {
                return null;
        }
        return param;
    }

}