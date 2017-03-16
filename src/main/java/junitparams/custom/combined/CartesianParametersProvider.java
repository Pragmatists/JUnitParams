package junitparams.custom.combined;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import junitparams.Parameters;
import junitparams.custom.ParametersProvider;
import junitparams.internal.parameters.*;
import org.junit.runners.model.FrameworkMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * {@link ParametersProvider} implementation supporting {@link CartesianParameters} which allow for a simpler transition
 * between {@link Parameters} and the cartesian product form.
 */
@SuppressWarnings("WeakerAccess")
public class CartesianParametersProvider implements ParametersProvider<CartesianParameters> {

    /**
     * A {@link ResultAdapter} to convert the collected parameters for cartesian permutation.
     */
    private static class CartesianResultAdapter implements ResultAdapter {
        @Override
        public Object[] adapt(Object result) {
            ImmutableList<?> listResult = asImmutableList(result);
            return listResult.toArray(new Object[listResult.size()]);
        }

        private ImmutableList<?> asImmutableList(Object result) {
            if (result instanceof Iterable) {
                return ImmutableList.copyOf((Iterable<?>) result);
            }
            if (result instanceof Iterator) {
                return ImmutableList.copyOf((Iterator<?>) result);
            }
            if (result.getClass().isArray()) {
                return ImmutableList.copyOf(((Object[]) result));
            }
            return ImmutableList.of(result);
        }
    }

    private final Class<?> testClass;

    private final FrameworkMethod frameworkMethod;

    private CartesianParameters parametersAnnotation;

    /**
     * Constructs a new provider of cartesian parameters.
     *
     * @param testClass       the context test class
     * @param frameworkMethod the context framework method
     */
    public CartesianParametersProvider(Class<?> testClass, FrameworkMethod frameworkMethod) {
        this.testClass = checkNotNull(testClass, "testClass must not be null");
        this.frameworkMethod = checkNotNull(frameworkMethod, "frameworkMethod must not be null");
    }

    @Override
    public Object[] getParameters() {
        ImmutableList<Object[]> objects = FluentIterable.from(Arrays.asList(parametersAnnotation.value()))
                .transform(new Function<Parameters, Object[]>() {
                    @Override
                    public Object[] apply(Parameters parameters) {
                        return ParametersFromParametersAnnotation.parametersFor(
                                testClass, frameworkMethod, parameters, new CartesianResultAdapter()
                        );
                    }
                }).toList();

        return Cartesian.getCartesianProductOf(new ArrayList<Object[]>(objects));
    }

    @Override
    public void initialize(CartesianParameters parametersAnnotation) {
        this.parametersAnnotation = checkNotNull(parametersAnnotation, "parametersAnnotation must not be null");
    }
}
