package junitparams;

/**
 * Some String utils to handle parameterised tests' results.
 * 
 * @author Pawel Lipinski
 */
class Utils {

    public static String stringify(Object paramSet, int paramIdx) {
        String result = "[" + paramIdx + "] ";

        if (paramSet instanceof String)
            result += paramSet;
        else
            result += asCsvString(safelyCastParamsToArray(paramSet), paramIdx);

        return trimSpecialChars(result);
    }

    private static String trimSpecialChars(String result) {
        return result.replace('\n', ' ').replace('(', '[').replace(')', ']');
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
        String result = "";

        for (Object param : params) {
            result = addParamToResult(result, param);

            if (param != params[params.length - 1])
                result += ", ";
        }

        return result;
    }

    private static String addParamToResult(String result, Object param) {
        if (param == null)
            result += "null";
        else {
            try {
                param.getClass().getDeclaredMethod("toString");
                result += param.toString();
            } catch (Exception e) {
                result += param.getClass().getSimpleName();
            }
        }
        return result;
    }
}
