package junitparams.internal.parameters;

import junitparams.internal.parameters.toarray.ParamsToArrayConverter;
import org.junit.runners.model.FrameworkMethod;

/**
 * A static utility class pertaining to {@link ResultAdapter} implementations.
 */
@SuppressWarnings("WeakerAccess")
public class ResultAdapters {
    /**
     * A result adapter for producing parameters for a {@link FrameworkMethod}.
     *
     * @param frameworkMethod the framework method
     * @return the result adapter
     */
    public static ResultAdapter adaptParametersFor(FrameworkMethod frameworkMethod) {
        return new ParamsToArrayResultAdapter(frameworkMethod);
    }

    private static class ParamsToArrayResultAdapter implements ResultAdapter {
        private final FrameworkMethod frameworkMethod;

        private ParamsToArrayResultAdapter(FrameworkMethod frameworkMethod) {
            this.frameworkMethod = frameworkMethod;
        }

        @Override
        public Object[] adapt(Object result) {
            return new ParamsToArrayConverter(
                    ImmutableParameterTypeSupplier.copyFrom(frameworkMethod)
            ).convert(result);
        }
    }

    private ResultAdapters() {
    }
}
