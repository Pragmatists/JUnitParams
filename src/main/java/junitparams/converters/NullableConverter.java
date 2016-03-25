package junitparams.converters;


public class NullableConverter implements Converter<Nullable, String>{
    
    private String nullIdentifier;
    
    public void initialize(Nullable annotation) {
        nullIdentifier = annotation.nullIdentifier() == null ? "null" : annotation.nullIdentifier();
    }

    public String convert(Object param) throws ConversionFailedException {
        if(param instanceof String && ((String)param).trim().equalsIgnoreCase(nullIdentifier)){
                return null;
        }
        return (String)param;
    }

}