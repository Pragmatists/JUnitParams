package junitparams.internal.parameters;

import com.google.common.collect.ImmutableList;
import org.junit.runners.model.FrameworkMethod;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * An immutable {@link ParameterTypeSupplier}.
 */
class ImmutableParameterTypeSupplier implements ParameterTypeSupplier {
    /**
     * Creates a new {@link ImmutableParameterTypeSupplier} from the provided {@link FrameworkMethod}'s parameter types.
     *
     * @param frameworkMethod the framework method to acquire the parameters of
     * @return an {@link ImmutableParameterTypeSupplier}
     */
    static ImmutableParameterTypeSupplier copyFrom(FrameworkMethod frameworkMethod) {
        return of(frameworkMethod.getMethod().getParameterTypes());
    }

    /**
     * Creates a new {@link ImmutableParameterTypeSupplier} from the provided parameter types.
     *
     * @param types the parameter types
     * @return an {@link ImmutableParameterTypeSupplier}
     */
    static ImmutableParameterTypeSupplier copyOf(Iterable<Class<?>> types) {
        return new ImmutableParameterTypeSupplier(types);
    }

    /**
     * Creates a new {@link ImmutableParameterTypeSupplier} as a copy of another {@link ParameterTypeSupplier}.
     *
     * @param original the original {@link ParameterTypeSupplier}
     * @return an {@link ImmutableParameterTypeSupplier}
     */
    static ImmutableParameterTypeSupplier copyOf(ParameterTypeSupplier original) {
        if (original instanceof ImmutableParameterTypeSupplier) {
            return (ImmutableParameterTypeSupplier) original;
        }
        return new ImmutableParameterTypeSupplier(original.getParameterTypes());
    }

    /**
     * Creates a new {@link ImmutableParameterTypeSupplier} from the provided parameter types.
     *
     * @param types the parameter types
     * @return an {@link ImmutableParameterTypeSupplier}
     */
    public static ImmutableParameterTypeSupplier of(Class<?>... types) {
        return new ImmutableParameterTypeSupplier(ImmutableList.copyOf(types));
    }

    private ImmutableList<Class<?>> types;

    private ImmutableParameterTypeSupplier(Iterable<Class<?>> types) {
        this.types = ImmutableList.copyOf(checkNotNull(types, "types must not be null"));
    }

    @Override
    public List<Class<?>> getParameterTypes() {
        return types;
    }
}
