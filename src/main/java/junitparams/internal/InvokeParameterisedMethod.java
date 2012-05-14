package junitparams.internal;

import java.util.regex.*;

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
        if (paramset[0] == null || testMethod.getMethod().getParameterTypes()[0].isPrimitive())
            return true;

        return testMethod.getMethod().getParameterTypes()[0].isAssignableFrom(paramset[0].getClass());
    }

    private Object[] castParamsFromString(String params) {
        Object[] columns = null;
        try {
            columns = parseStringToParams(params);

            Class<?>[] parameterTypes = testMethod.getMethod().getParameterTypes();

            verifySameSizeOfArrays(columns, parameterTypes);
            columns = castAllParametersToProperTypes(columns, parameterTypes);
        } catch (RuntimeException e) {
            new IllegalArgumentException("Cannot parse parameters. Did you use , as column separator? " + params, e).printStackTrace();
        }

        return columns;
    }

    private Object[] parseStringToParams(String params) {
        String[] colls = splitPattern.split(params);
        if (colls.length == 1 && "".equals(colls[0]))
            return new String[0];

        return colls;
    }

    private Object[] castAllParametersToProperTypes(Object[] columns, Class<?>[] parameterTypes) {
        Object[] result = new Object[columns.length];

        for (int i = 0; i < columns.length; i++)
            result[i] = castParameterFromString(columns[i], parameterTypes[i]);

        return result;
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
        testMethod.invokeExplosively(testClass, params);
    }
}
