package junitparams.internal.parameters;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;

import junitparams.Parameters;

import static com.google.common.base.Preconditions.checkNotNull;
import static junitparams.internal.parameters.ResultAdapters.adaptParametersFor;

/**
 * Common base class for obtaining parameters from a source class.
 */
abstract class ParamsFromMethodCommon implements ParametrizationStrategy {
    private final Parameters annotation;
    private final FrameworkMethod frameworkMethod;
    private final ResultAdapter resultAdapter;
    private final Class<?> sourceClass;

    ParamsFromMethodCommon(final FrameworkMethod frameworkMethod, Class<?> sourceClass, Parameters annotation) {
        this(frameworkMethod, sourceClass, annotation, adaptParametersFor(frameworkMethod));
    }

    ParamsFromMethodCommon(FrameworkMethod frameworkMethod, Class<?> sourceClass, Parameters annotation,
                           ResultAdapter resultAdapter) {
        this.frameworkMethod = checkNotNull(frameworkMethod, "frameworkMethod must not be null");
        this.sourceClass = checkNotNull(sourceClass, "sourceClass must not be null");
        this.annotation = checkNotNull(annotation, "annotation must not be null");
        this.resultAdapter = checkNotNull(resultAdapter, "resultAdapter must not be null");
    }

    @Override
    public Object[] getParameters() {
        String methodAnnotation = annotation.method();

        if (methodAnnotation.isEmpty()) {
            return invokeMethodWithParams(defaultMethodName(), sourceClass);
        }

        List<Object> result = new ArrayList<Object>();
        for (String methodName : methodAnnotation.split(",")) {
            Collections.addAll(result, invokeMethodWithParams(methodName.trim(), sourceClass));
        }

        return result.toArray();
    }

    boolean containsDefaultParametersProvidingMethod(Class<?> sourceClass) {
        return findMethodInTestClassHierarchy(defaultMethodName(), sourceClass) != null;
    }

    private String defaultMethodName() {
        return "parametersFor"
                + frameworkMethod.getName().substring(0, 1).toUpperCase()
                + this.frameworkMethod.getName().substring(1);
    }

    private Object[] invokeMethodWithParams(String methodName, Class<?> sourceClass) {
        Method providerMethod = findMethodInTestClassHierarchy(methodName, sourceClass);
        if (providerMethod == null) {
            throw new RuntimeException("Could not find method: " + methodName + " so no params were used.");
        }

        return invokeParamsProvidingMethod(providerMethod, sourceClass);
    }

    @SuppressWarnings("unchecked")
    private Object[] invokeParamsProvidingMethod(Method provideMethod, Class<?> sourceClass) {
        try {
            Constructor<?> constructor = sourceClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object testObject = constructor.newInstance();
            provideMethod.setAccessible(true);
            Object result = provideMethod.invoke(testObject);

            return resultAdapter.adapt(result);
        } catch (ClassCastException e) {
            throw new RuntimeException(
                "The return type of: " + provideMethod.getName() + " defined in class " + sourceClass +
                " should be one of the following:\nObject[][], Iterable<Object[]>, Iterable<Iterable<Object>>," +
                " Iterator<Object[]>.\nFix it!", e);
        } catch (Exception e) {
            throw new RuntimeException("Could not invoke method: " + provideMethod.getName() + " defined in class " +
                    sourceClass + " so no params were used.", e);
        }
    }

    private Method findMethodInTestClassHierarchy(String methodName, Class<?> sourceClass) {
        Class<?> declaringClass = sourceClass;
        while (declaringClass.getSuperclass() != null) {
            try {
                return declaringClass.getDeclaredMethod(methodName);
            } catch (Exception ignore) {
            }
            declaringClass = declaringClass.getSuperclass();
        }
        return null;
    }

}
