package junitparams.internal.parameters.toarray;

import org.junit.runners.model.FrameworkMethod;

import java.util.Arrays;
import java.util.List;

public class ParamsToArrayConverter {
    private FrameworkMethod frameworkMethod;

    public ParamsToArrayConverter(FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;
    }

    public Object[] convert(Object result) {
        // handle single iterable parameter result case where result is
        // assignable to the test method parameter
        if (frameworkMethod.getMethod().getParameterTypes().length == 1) {
            Class<?> type = frameworkMethod.getMethod().getParameterTypes()[0];
            if (type.isAssignableFrom(result.getClass())) {
                SimpleIterableResultToArray converter = new SimpleIterableResultToArray(result);
                if (converter.isApplicable()) {
                    return converter.convert();
                }
            }
        }

        List<? extends ResultToArray> converters = Arrays.asList(
                new ObjectArrayResultToArray(result, frameworkMethod),
                new IterableResultToArray(result),
                new IteratorResultToArray(result)
        );
        for (ResultToArray converter : converters) {
            if (converter.isApplicable()) {
                return converter.convert();
            }
        }

        throw new ClassCastException();
    }

}
