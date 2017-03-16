package junitparams.internal.parameters.toarray;

import com.google.common.collect.Iterables;
import junitparams.internal.parameters.ParameterTypeSupplier;

import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ParamsToArrayConverter {
    private ParameterTypeSupplier parameterTypeSupplier;

    public ParamsToArrayConverter(ParameterTypeSupplier parameterTypeSupplier) {
        this.parameterTypeSupplier = checkNotNull(parameterTypeSupplier, "parameterTypeSupplier must not be null");
    }

    public Object[] convert(Object result) {
        // handle single iterable parameter result case where result is
        // assignable to the test method parameter
        List<Class<?>> parameterTypes = parameterTypeSupplier.getParameterTypes();
        if (parameterTypes.size() == 1) {
            Class<?> type = Iterables.getOnlyElement(parameterTypes);
            if (type.isAssignableFrom(result.getClass())) {
                SimpleIterableResultToArray converter = new SimpleIterableResultToArray(result);
                if (converter.isApplicable()) {
                    return converter.convert();
                }
            }
        }

        List<? extends ResultToArray> converters = Arrays.asList(
                new ObjectArrayResultToArray(result, parameterTypeSupplier),
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
