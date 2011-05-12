package pragmatists.parameterised;

public class Utils {

    public static String stringify(Object paramSet) {
        if (paramSet instanceof String)
            return (String) paramSet;
        Object[] params = (Object[]) paramSet;

        return asCsvString(params);
    }

    private static String asCsvString(Object[] params) {
        String result = "";

        for (Object param : params) {
            result += param.toString();
            if (param != params[params.length - 1])
                result += ", ";
        }

        return result;
    }
}
