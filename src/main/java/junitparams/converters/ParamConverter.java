package junitparams.converters;

/**
 * 
 * Implement this interface if you want to convert params from some
 * representation to the type expected by your test method's parameter.
 * 
 * &lt;T&gt; is the expected parameter type.
 * 
 * @author Pawel Lipinski
 */
public interface ParamConverter<T> {
    T convert(Object param, String options) throws ConversionFailedException;
}
