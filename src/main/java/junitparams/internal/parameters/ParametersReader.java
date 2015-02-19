package junitparams.internal.parameters;

import static java.lang.String.*;
import static java.util.Arrays.*;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;

import junitparams.FileParameters;
import junitparams.Parameters;

public class ParametersReader {
    private final FrameworkMethod frameworkMethod;
    private final List<ParametrizationStrategy> strategies;

    public ParametersReader(Class<?> testClass, FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;

        strategies = asList(
                new ParametersFromValue(frameworkMethod),
                new ParametersFromExternalClassProvideMethod(frameworkMethod),
                new ParametersFromExternalClassMethod(frameworkMethod),
                new ParametersFromTestClassMethod(frameworkMethod, testClass),
                new ParametersFromFile(frameworkMethod));
    }

    public Object[] read() {
        ParametrizationStrategy strategy = findApplicableStrategy();
        if (strategy != null) {
            return strategy.getParameters();
        }
        return new Object[]{};
    }

    public boolean isParametrized() {
        return findApplicableStrategy() != null;
    }

    private ParametrizationStrategy findApplicableStrategy() {
        ParametrizationStrategy applicableStrategy = null;
        boolean strategyAlreadyFound = false;
        for (ParametrizationStrategy strategy : strategies) {
            if (strategy.isApplicable()) {
                if (strategyAlreadyFound) {
                    illegalState();
                }
                applicableStrategy = strategy;
                strategyAlreadyFound = true;
            }
        }
        return applicableStrategy;
    }

    private void illegalState() {
        throw new IllegalStateException( format("Illegal usage of JUnitParams in method %s. " +
                        "Check if you use only one supported parameters evaluation strategy. " +
                        "Common case is to use both %s and %s annotations.",
                        frameworkMethod.getName(), Parameters.class, FileParameters.class));
    }
}
