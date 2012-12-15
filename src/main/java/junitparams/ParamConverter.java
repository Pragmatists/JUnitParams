package junitparams;

/**
 * 
 * Implement this interface if you want to convert params from some
 * representation to the type expected by your test method's parameter.
 * 
 * <T> is the expected parameter type.
 * 
 * @author Pawel Lipinski
 */
public interface ParamConverter<T> {
    T convert(Object param);
}
