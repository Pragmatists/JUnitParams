package junitparams.internal.parameters;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import junitparams.custom.ParametersProvider;
import junitparams.internal.annotation.CustomParametersDescriptor;
import junitparams.internal.annotation.FrameworkMethodAnnotations;
import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static com.google.common.base.Preconditions.checkNotNull;

class ParametersFromCustomProvider implements ParametrizationStrategy {

    /**
     * A factory of {@link ParametersProvider} instances.
     */
    private abstract static class ParametersProviderFactory {

        private static final LoadingCache<Class<? extends ParametersProvider<? extends Annotation>>, ParametersProviderFactory> cache =
                CacheBuilder.newBuilder().build(
                        new CacheLoader<Class<? extends ParametersProvider<? extends Annotation>>, ParametersProviderFactory>() {
                            @Override
                            public ParametersProviderFactory load(Class<? extends ParametersProvider<? extends Annotation>> providerClass) throws Exception {
                                return resolveConstructor(providerClass);
                            }
                        }
                );

        /**
         * Creates a {@link ParametersProviderFactory} for the specified {@link ParametersProvider} class.
         *
         * @param providerClass the provider class
         * @param <T>           the provider class type
         * @return the factory
         */
        static <T extends ParametersProvider<? extends Annotation>> ParametersProviderFactory forType(Class<T> providerClass) {
            return cache.getUnchecked(providerClass);
        }

        private static <T extends ParametersProvider<? extends Annotation>> ParametersProviderFactory
        resolveConstructor(Class<T> providerClass) {
            try {
                final Constructor<T> cons = providerClass.getConstructor(Class.class, FrameworkMethod.class);
                return new ParametersProviderFactory() {
                    @Override
                    protected ParametersProvider<? extends Annotation> checkedNewInstance(Class<?> testClass, FrameworkMethod frameworkMethod)
                            throws IllegalAccessException, InvocationTargetException, InstantiationException {
                        checkNotNull(testClass, "testClass must not be null");
                        checkNotNull(frameworkMethod, "frameworkMethod must not be null");
                        return cons.newInstance(testClass, frameworkMethod);
                    }
                };
            } catch (NoSuchMethodException ignored) {
            }

            try {
                final Constructor<T> cons = providerClass.getConstructor(FrameworkMethod.class);
                return new ParametersProviderFactory() {
                    @Override
                    protected ParametersProvider<? extends Annotation> checkedNewInstance(Class<?> testClass, FrameworkMethod frameworkMethod)
                            throws IllegalAccessException, InvocationTargetException, InstantiationException {
                        checkNotNull(testClass, "testClass must not be null");
                        checkNotNull(frameworkMethod, "frameworkMethod must not be null");
                        return cons.newInstance(frameworkMethod);
                    }
                };
            } catch (NoSuchMethodException ignored) {
            }

            try {
                final Constructor<T> cons = providerClass.getConstructor(Class.class);
                return new ParametersProviderFactory() {
                    @Override
                    protected ParametersProvider<? extends Annotation> checkedNewInstance(Class<?> testClass, FrameworkMethod frameworkMethod)
                            throws IllegalAccessException, InvocationTargetException, InstantiationException {
                        checkNotNull(testClass, "testClass must not be null");
                        checkNotNull(frameworkMethod, "frameworkMethod must not be null");
                        return cons.newInstance(testClass);
                    }
                };
            } catch (NoSuchMethodException ignored) {
            }

            try {
                final Constructor<T> cons = providerClass.getConstructor();
                return new ParametersProviderFactory() {
                    @Override
                    protected ParametersProvider<? extends Annotation> checkedNewInstance(Class<?> testClass, FrameworkMethod frameworkMethod)
                            throws IllegalAccessException, InvocationTargetException, InstantiationException {
                        checkNotNull(testClass, "testClass must not be null");
                        checkNotNull(frameworkMethod, "frameworkMethod must not be null");
                        return cons.newInstance();
                    }
                };
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("Your Provider class must have a public no-arg constructor!", e);
            }
        }

        /**
         * Creates a new instance of the {@link ParametersProvider}.
         *
         * @param testClass       the test class
         * @param frameworkMethod the framework method
         * @return the new {@link ParametersProvider} instance
         * @throws IllegalAccessException    if the constructor is not accessible
         * @throws InvocationTargetException if the constructor throws an exception
         * @throws InstantiationException    if the class cannot be instantiated
         */
        protected abstract ParametersProvider<? extends Annotation> checkedNewInstance(Class<?> testClass, FrameworkMethod frameworkMethod)
                throws IllegalAccessException, InvocationTargetException, InstantiationException;

        /**
         * Creates a new instance of the {@link ParametersProvider}.
         *
         * @param testClass       the test class
         * @param frameworkMethod the framework method
         * @return the new {@link ParametersProvider} instance
         */
        public ParametersProvider<? extends Annotation> newInstance(Class<?> testClass, FrameworkMethod frameworkMethod) {
            try {
                return checkedNewInstance(testClass, frameworkMethod);
            } catch (InvocationTargetException e) {
                if (e.getCause() != null) {
                    throw Throwables.propagate(e.getCause());
                }
                throw Throwables.propagate(e);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }

    private final Class<?> testClass;
    private final FrameworkMethod frameworkMethod;
    private final FrameworkMethodAnnotations frameworkMethodAnnotations;

    ParametersFromCustomProvider(Class<?> testClass, FrameworkMethod frameworkMethod) {
        this.testClass = checkNotNull(testClass, "testClass must not be null");
        this.frameworkMethod = checkNotNull(frameworkMethod, "frameworkMethod must not be null");
        frameworkMethodAnnotations = new FrameworkMethodAnnotations(frameworkMethod);
    }

    @Override
    public boolean isApplicable() {
        return frameworkMethodAnnotations.hasCustomParameters();
    }

    @Override
    public Object[] getParameters() {
        CustomParametersDescriptor parameters = frameworkMethodAnnotations.getCustomParameters();

        // this cast is type-safe
        @SuppressWarnings("unchecked") Class<? extends ParametersProvider<Annotation>> providerClass =
                (Class<? extends ParametersProvider<Annotation>>) parameters.provider();

        ParametersProvider<Annotation> provider = instantiate(providerClass);
        provider.initialize(parameters.annotation());
        return provider.getParameters();
    }

    @SuppressWarnings("unchecked")
    private ParametersProvider<Annotation> instantiate(final Class<? extends ParametersProvider<?>> providerClass) {
        checkNotNull(providerClass, "providerClass must not be null");

        return (ParametersProvider<Annotation>) ParametersProviderFactory
                .forType(providerClass)
                .newInstance(testClass, frameworkMethod);
    }
}
