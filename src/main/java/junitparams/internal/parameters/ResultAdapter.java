package junitparams.internal.parameters;

/**
 * Adapts a result to an array of members.
 */
public interface ResultAdapter {
    /**
     * Adapt a result object to an array of members.
     *
     * @param result the result object
     * @return the array of members
     */
    Object[] adapt(Object result);
}
