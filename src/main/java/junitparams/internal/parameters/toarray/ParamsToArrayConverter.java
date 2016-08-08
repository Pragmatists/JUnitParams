package junitparams.internal.parameters.toarray;

import java.util.Arrays;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;

public class ParamsToArrayConverter {
    private FrameworkMethod frameworkMethod;

    public ParamsToArrayConverter(FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;
    }

    public Object[] convert(Object result) {
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
