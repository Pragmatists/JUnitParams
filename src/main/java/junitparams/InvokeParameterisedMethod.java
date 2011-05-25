package junitparams;

import java.util.*;

import org.junit.runners.model.*;

/**
 * JUnit invoker for parameterised test methods
 * 
 * @author Pawel Lipinski
 */
public class InvokeParameterisedMethod extends Statement {

    private final Object[] params;
    private final FrameworkMethod testMethod;
    private final Object target;
    private final String paramsAsString;

    public String getParamsAsString() {
        return paramsAsString;
    }

    public InvokeParameterisedMethod(FrameworkMethod testMethod, Object target, Object params, int paramSetIdx) {
        this.testMethod = testMethod;
        this.target = target;
        if (params instanceof String) {
            paramsAsString = Utils.stringify(params, paramSetIdx - 1);
            this.params = castParamsFromString((String) params);
        } else {
            paramsAsString = Utils.stringify(params, paramSetIdx - 1);
            this.params = Utils.safelyCastParamsToArray(params);
        }
    }

    private Object[] castParamsFromString(String params) {
        Object[] columns = null;
        try {
            columns = parseStringToParams(params);

            Class<?>[] parameterTypes = testMethod.getMethod().getParameterTypes();

            verifySameSizeOfArrays(columns, parameterTypes);
            columns = castColumns(columns, parameterTypes);
        } catch (RuntimeException e) {
            new IllegalArgumentException("Cannot parse parameters. Did you use , as column separator? " + params, e).printStackTrace();
        }

        return columns;
    }

    private Object[] parseStringToParams(String params) {
        Object[] columns;
        StringTokenizer tokenizer = new StringTokenizer(params, ",");
        List<String> cols = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
            String nextToken = tokenizer.nextToken().trim();
            cols.add(nextToken);
        }
        columns = cols.toArray(new String[] {});
        return columns;
    }

    private Object[] castColumns(Object[] columns, Class<?>[] parameterTypes) {
        Object[] result = new Object[columns.length];

        for (int i = 0; i < columns.length; i++)
            result[i] = castColumn(columns[i], parameterTypes[i]);

        return result;
    }

    private Object castColumn(Object object, Class<?> clazz) {
        if (clazz.isInstance(object))
            return object;
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
                    "Number of parameters inside @Params annotation doesn't match the number of test method parameters.\nThere are "
                            + columns.length + " parameters in annotation, while there's " + parameterTypes.length + " parameters in the "
                            + testMethod.getName() + " method.");
    }

    @Override
    public void evaluate() throws Throwable {
        testMethod.invokeExplosively(target, params);
    }
}
