package junitparams.internal.parameters;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;

import junitparams.Parameters;

class ParamsFromMethodCommon {
    private FrameworkMethod frameworkMethod;

    ParamsFromMethodCommon(FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;
    }

    Object[] paramsFromMethod(Class<?> sourceClass) {
        String methodAnnotation = frameworkMethod.getAnnotation(Parameters.class).method();

        if (methodAnnotation.isEmpty()) {
            return invokeMethodWithParams(defaultMethodName(), sourceClass);
        }

        List<Object> result = new ArrayList<Object>();
        for (String methodName : methodAnnotation.split(",")) {
            for (Object param : invokeMethodWithParams(methodName.trim(), sourceClass))
                result.add(param);
        }

        return result.toArray();
    }

    Object[] getDataFromMethod(Method providerMethod) throws IllegalAccessException, InvocationTargetException {
        return encapsulateParamsIntoArrayIfSingleParamsetPassed((Object[]) providerMethod.invoke(null));
    }

    boolean containsDefaultParametersProvidingMethod(Class<?> sourceClass) {
        return findMethodInTestClassHierarchy(defaultMethodName(), sourceClass) != null;
    }

    private String defaultMethodName() {
        return "parametersFor" + frameworkMethod.getName().substring(0, 1).toUpperCase()
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
            Object testObject = sourceClass.newInstance();
            provideMethod.setAccessible(true);
            Object result = provideMethod.invoke(testObject);

            if (Object[].class.isAssignableFrom(result.getClass())) {
                Object[] params = (Object[]) result;
                return encapsulateParamsIntoArrayIfSingleParamsetPassed(params);
            }

            if (Iterable.class.isAssignableFrom(result.getClass())) {
                try {
                    ArrayList<Object[]> res = new ArrayList<Object[]>();
                    for (Object[] paramSet : (Iterable<Object[]>) result)
                        res.add(paramSet);
                    return res.toArray();
                } catch (ClassCastException e1) {
                    // Iterable with consecutive paramsets, each of one param
                    ArrayList<Object> res = new ArrayList<Object>();
                    for (Object param : (Iterable<?>) result)
                        res.add(new Object[]{param});
                    return res.toArray();
                }
            }

            if (Iterator.class.isAssignableFrom(result.getClass())) {
                Object iteratedElement = null;
                try {
                    ArrayList<Object[]> res = new ArrayList<Object[]>();
                    Iterator<Object[]> iterator = (Iterator<Object[]>) result;
                    while (iterator.hasNext()) {
                        iteratedElement = iterator.next();
                        // ClassCastException will occur in the following line
                        // if the iterator is actually Iterator<Object> in Java 7
                        res.add((Object[]) iteratedElement);
                    }
                    return res.toArray();
                } catch (ClassCastException e1) {
                    // Iterator with consecutive paramsets, each of one param
                    ArrayList<Object> res = new ArrayList<Object>();
                    Iterator<?> iterator = (Iterator<?>) result;
                    // The first element is already stored in iteratedElement
                    res.add(iteratedElement);
                    while (iterator.hasNext()) {
                        res.add(new Object[]{iterator.next()});
                    }
                    return res.toArray();
                }
            }

            throw new ClassCastException();

        } catch (ClassCastException e) {
            throw new RuntimeException("The return type of: " + provideMethod.getName() + " defined in class " +
                    sourceClass + " is not Object[][] nor Iterable<Object[]>. Fix it!", e);
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

    private Object[] encapsulateParamsIntoArrayIfSingleParamsetPassed(Object[] params) {
        if (frameworkMethod.getMethod().getParameterTypes().length != params.length) {
            return params;
        }

        if (params.length == 0) {
            return params;
        }

        Object param = params[0];
        if (param == null || !param.getClass().isArray()) {
            return new Object[]{params};
        }

        return params;
    }

}
