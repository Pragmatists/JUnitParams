package junitparams.internal.parameters;

import java.util.List;

import org.junit.runners.model.FrameworkMethod;

import junitparams.FileParameters;
import junitparams.Parameters;

import static java.lang.String.*;
import static java.util.Arrays.*;

public class ParametersReader {

    public static final String ILLEGAL_STATE_EXCEPTION_MESSAGE
        = format("Illegal usage of JUnitParams in method %s. " +
                 "Check that you have only used one supported parameters evaluation strategy. " +
                 "Common case is to use both %s and %s annotations.",
                 "%s", Parameters.class, FileParameters.class);

    private final FrameworkMethod frameworkMethod;
    private final List<ParametrizationStrategy> strategies;

    public ParametersReader(Class<?> testClass, FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;

        strategies = asList(
                new ParametersFromCustomProvider(frameworkMethod),
                new ParametersFromValue(frameworkMethod),
                new ParametersFromExternalClassProvideMethod(frameworkMethod),
                new ParametersFromExternalClassMethod(frameworkMethod),
                new ParametersFromTestClassMethod(frameworkMethod, testClass)
        );
    }

    public Object[] read() {
        boolean strategyAlreadyFound = false;
        Object[] parameters = new Object[]{};

        for (ParametrizationStrategy strategy : strategies) {
            if (strategy.isApplicable()) {
                if (strategyAlreadyFound) {
                    illegalState();
                }
                parameters = strategy.getParameters();
                strategyAlreadyFound = true;
            }
        }
        return parameters;
    }

    private void illegalState() {
        throw new IllegalStateException(format(ILLEGAL_STATE_EXCEPTION_MESSAGE, frameworkMethod.getName()));
    }
}
