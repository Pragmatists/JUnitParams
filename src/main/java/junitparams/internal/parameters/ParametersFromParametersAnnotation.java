package junitparams.internal.parameters;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import junitparams.Parameters;
import org.junit.runners.model.FrameworkMethod;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static junitparams.internal.parameters.ParametersReader.ILLEGAL_STATE_EXCEPTION_MESSAGE;

/**
 * A {@link ParametrizationStrategy} for reading parameters given a {@link Parameters} annotation.
 */
public class ParametersFromParametersAnnotation implements ParametrizationStrategy {
    private static final Predicate<ParametrizationStrategy> STRATEGY_IS_APPLICABLE =
            new Predicate<ParametrizationStrategy>() {
                @Override
                public boolean apply(ParametrizationStrategy strategy) {
                    return strategy.isApplicable();
                }
            };

    private final ImmutableList<ParametrizationStrategy> strategies;
    private final FrameworkMethod frameworkMethod;
    private final Class<?> testClass;
    private final Parameters annotation;

    /**
     * Read the array of parameters defined by the {@link Parameters} annotation in the current context.
     *
     * @param testClass       the context test class
     * @param frameworkMethod th context test method
     * @param annotation      the annotation to read
     * @return the array of parameters
     * @throws IllegalArgumentException if the {@link Parameters} annotation is invalid
     */
    public static Object[] parametersFor(Class<?> testClass, FrameworkMethod frameworkMethod, Parameters annotation, ResultAdapter resultAdapter) {
        return ParametersFromParametersAnnotation.create(testClass, frameworkMethod, annotation, resultAdapter).getParameters();
    }

    /**
     * Create a reader of {@link Parameters} annotation values.
     *
     * @param testClass       the context test class
     * @param frameworkMethod th context test method
     * @param annotation      the annotation to read
     * @return the array of parameters
     * @throws IllegalArgumentException if the {@link Parameters} annotation is invalid
     */
    static ParametersFromParametersAnnotation create(
            Class<?> testClass, final FrameworkMethod frameworkMethod, Parameters annotation) {
        return create(testClass, frameworkMethod, annotation, ResultAdapters.adaptParametersFor(frameworkMethod));
    }

    /**
     * Create a reader of {@link Parameters} annotation values.
     *
     * @param testClass       the context test class
     * @param frameworkMethod th context test method
     * @param annotation      the annotation to read
     * @param resultAdapter   the result adapter
     * @return the array of parameters
     * @throws IllegalArgumentException if the {@link Parameters} annotation is invalid
     */
    private static ParametersFromParametersAnnotation create(
            Class<?> testClass, FrameworkMethod frameworkMethod, Parameters annotation, ResultAdapter resultAdapter) {
        return new ParametersFromParametersAnnotation(testClass, frameworkMethod, annotation, resultAdapter);
    }

    private ParametersFromParametersAnnotation(Class<?> testClass, FrameworkMethod frameworkMethod, Parameters annotation,
                                               ResultAdapter resultAdapter) {
        this.testClass = checkNotNull(testClass, "testClass must not be null");
        this.frameworkMethod = checkNotNull(frameworkMethod, "frameworkMethod must not be null");
        this.annotation = checkNotNull(annotation, "annotation must not be null");

        this.strategies = ImmutableList.of(
                new ParametersFromValue(annotation),
                new ParametersFromExternalClassProvideMethod(frameworkMethod, annotation),
                new ParametersFromExternalClassMethod(frameworkMethod, annotation, resultAdapter),
                new ParametersFromTestClassMethod(testClass, frameworkMethod, annotation, resultAdapter)
        );
    }

    @Override
    public Object[] getParameters() {
        ImmutableList<ParametrizationStrategy> matchingStrategies = FluentIterable.from(strategies)
                .filter(STRATEGY_IS_APPLICABLE)
                .toList();
        if (matchingStrategies.size() == 1) {
            return Iterables.getOnlyElement(matchingStrategies).getParameters();
        }
        throw new IllegalStateException(format(ILLEGAL_STATE_EXCEPTION_MESSAGE, frameworkMethod.getName()));
    }

    @Override
    public boolean isApplicable() {
        return Iterables.any(strategies, STRATEGY_IS_APPLICABLE);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(ParametersFromParametersAnnotation.class)
                .add("testClass", testClass)
                .add("frameworkMethod", frameworkMethod)
                .add("annotation", annotation)
                .toString();
    }
}
