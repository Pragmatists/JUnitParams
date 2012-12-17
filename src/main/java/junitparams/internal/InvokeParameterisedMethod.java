package junitparams.internal;

import java.lang.annotation.*;
import java.util.*;
import java.util.regex.*;

import junitparams.converters.*;

import org.junit.runners.model.*;

/**
 * JUnit invoker for parameterised test methods
 * 
 * @author Pawel Lipinski
 */
public class InvokeParameterisedMethod extends Statement {

    private final Object[] params;
    private final FrameworkMethod testMethod;
    private final Object testClass;
    private final String paramsAsString;

    private static Pattern splitPattern = Pattern.compile("\\s*(?<!\\\\)[|,]\\s*");

    public String getParamsAsString() {
        return paramsAsString;
    }

    public InvokeParameterisedMethod(FrameworkMethod testMethod, Object testClass, Object params, int paramSetIdx) {
        this.testMethod = testMethod;
        this.testClass = testClass;
        paramsAsString = Utils.stringify(params, paramSetIdx - 1);
        if (params instanceof String)
            this.params = castParamsFromString((String) params);
        else {
            this.params = castParamsFromObjects(params);
        }
    }

    private Object[] castParamsFromObjects(Object params) {
        Object[] paramset = Utils.safelyCastParamsToArray(params);

        if (isFirstParamSameTypeAsExpected(paramset))
            return paramset;

        Class<?>[] typesOfParameters = createArrayOfTypesOf(paramset);

        Object resultParam = createObjectOfExpectedTypeBasedOnParams(paramset, typesOfParameters);

        return new Object[] { resultParam };
    }

    private Object createObjectOfExpectedTypeBasedOnParams(Object[] paramset, Class<?>[] typesOfParameters) {
        Object resultParam;
        try {
            resultParam = testMethod.getMethod().getParameterTypes()[0].getConstructor(typesOfParameters).newInstance(paramset);
        } catch (Exception e) {
            throw new IllegalStateException("While trying to create object of class " + testMethod.getMethod().getParameterTypes()[0]
                + " could not find constructor with arguments matching (type-wise) the ones given in parameters.", e);
        }
        return resultParam;
    }

    private Class<?>[] createArrayOfTypesOf(Object[] paramset) {
        Class<?>[] parametersBasedOnValues = new Class<?>[paramset.length];
        for (int i = 0; i < paramset.length; i++) {
            parametersBasedOnValues[i] = paramset[i].getClass();
        }
        return parametersBasedOnValues;
    }

    private boolean isFirstParamSameTypeAsExpected(Object[] paramset) {
        if (paramset == null || paramset[0] == null || testMethod.getMethod().getParameterTypes()[0].isPrimitive())
            return true;

        return testMethod.getMethod().getParameterTypes()[0].isAssignableFrom(paramset[0].getClass());
    }

    private Object[] castParamsFromString(String params) {
        Object[] columns = null;
        try {
            Class<?>[] parameterTypes = testMethod.getMethod().getParameterTypes();
            Annotation[][] parameterAnnotations = testMethod.getMethod().getParameterAnnotations();
            columns = parseStringToParams(params, parameterTypes.length);
            verifySameSizeOfArrays(columns, parameterTypes);
            columns = castAllParametersToProperTypes(columns, parameterTypes, parameterAnnotations);
        } catch (RuntimeException e) {
            new IllegalArgumentException("Cannot parse parameters. Did you use , as column separator? " + params, e).printStackTrace();
        }

        return columns;
    }

    private Object[] parseStringToParams(String params, int numberOfParams) {
        String[] colls = splitPattern.split(params);
        if (colls.length == 1 && "".equals(colls[0]))
            return new String[0];

        if ((numberOfParams == colls.length + 1) && (params.charAt(params.length() - 1) == ',')) {
            String[] tmp = Arrays.copyOf(colls, colls.length + 1);
            tmp[colls.length] = "";
            colls = tmp;
        }

        return colls;
    }

    private Object[] castAllParametersToProperTypes(Object[] columns, Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        Object[] result = new Object[columns.length];

        for (int i = 0; i < columns.length; i++) {
            if (parameterAnnotations[i].length == 0)
                result[i] = castParameterFromString(columns[i], parameterTypes[i]);
            else
                result[i] = castParameterUsingConverter(columns[i], parameterTypes[i], parameterAnnotations[i]);
        }

        return result;
    }

    private Object castParameterUsingConverter(Object param, Class<?> expectedType, Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAssignableFrom(ConvertParam.class)) {
                Class<? extends ParamConverter<?>> converterClass = ((ConvertParam) annotation).value();
                String options = ((ConvertParam) annotation).options();
                try {
                    return converterClass.newInstance().convert(param, options);
                } catch (Exception e) {
                    throw new RuntimeException("Your ParamConverter class must have a public no-arg constructor!", e);
                }
            }
        }
        throw new RuntimeException("Only @ConvertParam annotation is allowed on parameters!");
    }

    @SuppressWarnings("unchecked")
    private Object castParameterFromString(Object object, Class clazz) {
        if (clazz.isInstance(object))
            return object;
        if (clazz.isEnum())
            return (Enum.valueOf(clazz, (String) object));
        if (clazz.isAssignableFrom(String.class))
            return object.toString();
        if (clazz.isAssignableFrom(Integer.TYPE))
            return Integer.parseInt((String) object);
        if (clazz.isAssignableFrom(Short.TYPE))
            return Short.parseShort((String) object);
        if (clazz.isAssignableFrom(Long.TYPE))
            return Long.parseLong((String) object);
        if (clazz.isAssignableFrom(Float.TYPE))
            return Float.parseFloat((String) object);
        if (clazz.isAssignableFrom(Double.TYPE))
            return Double.parseDouble((String) object);
        if (clazz.isAssignableFrom(Boolean.TYPE))
            return Boolean.parseBoolean((String) object);
        if (clazz.isAssignableFrom(Character.TYPE))
            return object.toString().charAt(0);
        if (clazz.isAssignableFrom(Byte.TYPE))
            return Byte.parseByte((String) object);
        throw new IllegalArgumentException("Parameter type cannot be handled! Only primitive types and Strings can be used.");
    }

    private void verifySameSizeOfArrays(Object[] columns, Class<?>[] parameterTypes) {
        if (parameterTypes.length != columns.length)
            throw new IllegalArgumentException(
                "Number of parameters inside @Parameters annotation doesn't match the number of test method parameters.\nThere are "
                    + columns.length + " parameters in annotation, while there's " + parameterTypes.length + " parameters in the "
                    + testMethod.getName() + " method.");
    }

    @Override
    public void evaluate() throws Throwable {
        testMethod.invokeExplosively(testClass, params == null ? new Object[] { params } : params);
    }
}
