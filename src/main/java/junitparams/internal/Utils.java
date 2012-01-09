package junitparams.internal;

import java.lang.reflect.*;

/**
 * Some String utils to handle parameterised tests' results.
 * 
 * @author Pawel Lipinski
 */
public class Utils {

    public static String stringify(Object paramSet, int paramIdx) {
        String result = "[" + paramIdx + "] ";

        if (paramSet instanceof String)
            result += paramSet;
        else
            result += asCsvString(safelyCastParamsToArray(paramSet), paramIdx);

        return trimSpecialChars(result);
    }

    private static String trimSpecialChars(String result) {
        return result.replace(System.getProperty("line.separator"), " ")
                .replace('(', '[').replace(')', ']');
    }

    static Object[] safelyCastParamsToArray(Object paramSet) {
        Object[] params;
        try {
            params = (Object[]) paramSet;
        } catch (ClassCastException e) {
            params = new Object[] { paramSet };
        }
        return params;
    }

    private static String asCsvString(Object[] params, int paramIdx) {
        if (params.length == 0)
            return "";

        String result = "";
        Object lastParam = params[params.length - 1];

        for (Object param : params) {
            result = addParamToResult(result, param);

            if (param != lastParam)
                result += ", ";
        }

        return result;
    }

    private static String addParamToResult(String result, Object param) {
        if (param == null)
            result += "null";
        else {
            try {
                tryFindingOverridenToString(param);
                result += param.toString();
            } catch (Exception e) {
                result += param.getClass().getSimpleName();
            }
        }
        return result;
    }

    private static void tryFindingOverridenToString(Object param)
            throws NoSuchMethodException {
        final Method toString = param.getClass().getMethod("toString");

        if (toString.getDeclaringClass().equals(Object.class)) {
            throw new NoSuchMethodException();
        }
    }
}
