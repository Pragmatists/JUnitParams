package junitparams.internal.parameters;

import org.junit.runners.model.FrameworkMethod;

import java.util.List;

/**
 * A supplier of {@link ParametrizationStrategy} instances.
 */
public interface ParametrizationStrategyFactory {
    /**
     * Creates a set of parameterization strategies.
     *
     * @param testClass       the context test class
     * @param frameworkMethod the context framework method
     * @return the list of strategies
     */
    List<ParametrizationStrategy> createStrategies(Class<?> testClass, FrameworkMethod frameworkMethod);
}
