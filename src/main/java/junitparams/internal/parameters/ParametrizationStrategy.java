package junitparams.internal.parameters;

public interface ParametrizationStrategy {
    Object[] getParameters();
    boolean isApplicable();
}
