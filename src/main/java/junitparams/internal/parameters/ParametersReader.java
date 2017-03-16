package junitparams.internal.parameters;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import junitparams.FileParameters;
import junitparams.Parameters;
import org.junit.runners.model.FrameworkMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

import static java.lang.String.format;

public class ParametersReader {

    public static final String ILLEGAL_STATE_EXCEPTION_MESSAGE
        = format("Illegal usage of JUnitParams in method %s. " +
                 "Check that you have only used one supported parameters evaluation strategy. " +
                 "Common case is to use both %s and %s annotations.",
                 "%s", Parameters.class, FileParameters.class);

    private final FrameworkMethod frameworkMethod;
    private final List<ParametrizationStrategy> strategies;

    public ParametersReader(final Class<?> testClass, final FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;

        ServiceLoader<ParametrizationStrategyFactory> parametrizationStrategyServiceLoader =
                ServiceLoader.load(ParametrizationStrategyFactory.class);

        strategies = FluentIterable.from(parametrizationStrategyServiceLoader)
                .transformAndConcat(new Function<ParametrizationStrategyFactory, Iterable<ParametrizationStrategy>>() {
                    @Override
                    public Iterable<ParametrizationStrategy> apply(ParametrizationStrategyFactory factory) {
                        return factory.createStrategies(testClass, frameworkMethod);
                    }
                })
                .toList();
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
        if (!strategyAlreadyFound) {
            noStrategyFound();
        }
        return parameters;
    }

    private void noStrategyFound() {
        throw new IllegalStateException(format("Method %s#%s is annotated with @Parameters but there were no parameters provided.",
                frameworkMethod.getDeclaringClass().getName(), frameworkMethod.getName()));
    }

    private void illegalState() {
        throw new IllegalStateException(format(ILLEGAL_STATE_EXCEPTION_MESSAGE, frameworkMethod.getName()));
    }
}
