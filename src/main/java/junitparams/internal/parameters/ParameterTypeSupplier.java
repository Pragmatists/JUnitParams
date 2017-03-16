package junitparams.internal.parameters;

import java.util.List;

/**
 * A supplier of parameter types.
 */
public interface ParameterTypeSupplier {
    /**
     * A list of parameter types.
     *
     * @return the parameter types
     */
    List<Class<?>> getParameterTypes();
}
