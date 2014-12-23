package junitparams.internal.parameters;

interface ParametrizationStrategy {
    Object[] getParameters();
    boolean isApplicable();
}
