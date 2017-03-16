package junitparams.internal.parameters;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import junitparams.Parameters;
import org.junit.runners.model.FrameworkMethod;

import java.util.List;

/**
 * The default internal {@link ParametrizationStrategyFactory}.
 */
@AutoService(ParametrizationStrategyFactory.class)
public class DefaultParameterizationStrategyFactory implements ParametrizationStrategyFactory {
    @Override
    public List<ParametrizationStrategy> createStrategies(Class<?> testClass, FrameworkMethod frameworkMethod) {
        ImmutableList.Builder<ParametrizationStrategy> builder = ImmutableList.<ParametrizationStrategy>builder()
                .add(new ParametersFromCustomProvider(testClass, frameworkMethod));

        /* @Nullable */ Parameters annotation = frameworkMethod.getAnnotation(Parameters.class);
        if (annotation != null) {
            builder.add(ParametersFromParametersAnnotation.create(testClass, frameworkMethod, annotation));
        }
        return builder.build();
    }
}
