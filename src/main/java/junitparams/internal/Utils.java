package junitparams.internal;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import junitparams.converters.Nullable;

/**
 * Some String utils to handle parameterised tests' results.
 *
 * @author Pawel Lipinski
 */
public class Utils {
    private static final String REGEX_ALL_NEWLINES = "(\\r\\n|\\n|\\r)";

    public static String stringify(Object paramSet, int paramIdx) {
        String result = "[" + paramIdx + "] ";

        return result + stringify(paramSet);
    }

    public static String stringify(Object paramSet) {
        String result;
        if (paramSet == null)
            result = "null";
        else if (paramSet instanceof String)
            result = paramSet.toString();
        else
            result = asCsvString(safelyCastParamsToArray(paramSet));

        return trimSpecialChars(result);
    }

    public static String getParameterStringByIndexOrEmpty(Object paramSet, int parameterIndex) {
        Object[] params = safelyCastParamsToArray(paramSet);
        if (paramSet instanceof String) {
            params = splitAtCommaOrPipe((String) paramSet);
        }
        if (parameterIndex >= 0 && parameterIndex < params.length) {
            return addParamToResult("", params[parameterIndex]);
        }

        return "";
    }

    public static String[] splitAtCommaOrPipe(String input) {
        ArrayList<String> result = new ArrayList<String>();

        char character = '\0';
        char previousCharacter;

        StringBuilder value = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            previousCharacter = character;
            character = input.charAt(i);

            if (character == ',' || character == '|') {
                if (previousCharacter == '\\') {
                    value.setCharAt(value.length() - 1, character);
                    continue;
                }
                result.add(value.toString().trim());
                value = new StringBuilder();
                continue;
            }

            value.append(character);
        }
        result.add(value.toString().trim());

        return result.toArray(new String[]{});
    }
    
    
    @SuppressWarnings("unchecked")
    public static Object doCast(Object object, Class clazz){
    	 if (object == null || clazz.isInstance(object) || (!(object instanceof String) && clazz.isPrimitive()))
             return object;
         if (clazz.isEnum())
             return (Enum.valueOf(clazz, (String) object));
         if (clazz.isAssignableFrom(String.class))
             return object.toString();
         if (clazz.isAssignableFrom(Class.class))
             try {
                 return Class.forName((String) object);
             } catch (ClassNotFoundException e) {
                 throw new IllegalArgumentException("Parameter class (" + object + ") not found", e);
             }
         if (clazz.isAssignableFrom(Integer.TYPE) || clazz.isAssignableFrom(Integer.class))
             return Integer.parseInt((String) object);
         if (clazz.isAssignableFrom(Short.TYPE) || clazz.isAssignableFrom(Short.class))
             return Short.parseShort((String) object);
         if (clazz.isAssignableFrom(Long.TYPE) || clazz.isAssignableFrom(Long.class))
             return Long.parseLong((String) object);
         if (clazz.isAssignableFrom(Float.TYPE) || clazz.isAssignableFrom(Float.class))
             return Float.parseFloat((String) object);
         if (clazz.isAssignableFrom(Double.TYPE) || clazz.isAssignableFrom(Double.class))
             return Double.parseDouble((String) object);
         if (clazz.isAssignableFrom(Boolean.TYPE) || clazz.isAssignableFrom(Boolean.class))
             return Boolean.parseBoolean((String) object);
         if (clazz.isAssignableFrom(Character.TYPE) || clazz.isAssignableFrom(Character.class))
             return object.toString().charAt(0);
         if (clazz.isAssignableFrom(Byte.TYPE) || clazz.isAssignableFrom(Byte.class))
             return Byte.parseByte((String) object);
         if (clazz.isAssignableFrom(BigDecimal.class))
             return new BigDecimal((String) object);
         PropertyEditor editor = PropertyEditorManager.findEditor(clazz);
         if (editor != null) {
             editor.setAsText((String) object);
             return editor.getValue();
         }
         throw new IllegalArgumentException("Parameter type (" + clazz.getName() + ") cannot be handled!" +
                 " Only primitive types, BigDecimals and Strings can be used.");
    }
    
    public static Object makeNullable(Object value, Nullable annotation){
    	String nullIdentifier = determineNullIdentifier(annotation);
    	if(value instanceof String && ((String)value).trim().equalsIgnoreCase(nullIdentifier)){
    		return null;
    	}
    	return value;
    }
    
    private static String determineNullIdentifier( Nullable annotation){
    	String nullIdentifier = annotation.value();
    	if( ! Nullable.DEFAULT_NULL_IDENTIFIER.equals(nullIdentifier)){
    		return nullIdentifier;
    	}
    	return annotation.nullIdentifier();
    }
    
    private static String trimSpecialChars(String result) {
        return result.replace('(', '[').replace(')', ']').replaceAll(REGEX_ALL_NEWLINES, " ");
    }

    static Object[] safelyCastParamsToArray(Object paramSet) {
        final Object[] params;
        if (paramSet instanceof Object[]) {
            params = (Object[]) paramSet;
        } else {
            params = new Object[]{paramSet};
        }
        return params;
    }

    private static String asCsvString(Object[] params) {
        if (params == null)
            return "null";

        if (params.length == 0)
            return "";

        String result = "";

        for (int i = 0; i < params.length - 1; i++) {
            Object param = params[i];
            result = addParamToResult(result, param) + ", ";
        }
        result = addParamToResult(result, params[params.length - 1]);

        return result;
    }

    private static String addParamToResult(String result, Object param) {
        if (param == null)
            result += "null";
        else if (param.getClass().isArray())
            result += convertAnyArrayToString(param);
        else if (hasOverridenToStringMethod(param))
            result += param.toString();
        else
            result += param.getClass().getSimpleName();

        return result;
    }

    private static boolean hasOverridenToStringMethod(Object param) {
        Method[] methods = param.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("toString") && overridesMethod(method)) {
                return true;
            }
        }
        return false;
    }

    private static boolean overridesMethod(Method method) {
        return !method.getDeclaringClass().equals(Object.class);
    }

    static String uniqueMethodId(int index, Object paramSet, String methodName) {
        return stringify(paramSet, index) + " (" + methodName + ")";
    }

    private static String convertAnyArrayToString(Object arrayAsObject) {
        if (arrayAsObject.getClass().getComponentType().isPrimitive()) {
            return convertFromArrayOfPrimitives(arrayAsObject);
        } else {
            return Arrays.toString((Object[]) arrayAsObject);
        }
    }

    private static String convertFromArrayOfPrimitives(Object arrayOfPrimitives) {
        String componentType = arrayOfPrimitives.getClass().getComponentType().getName();
        if ("byte".equals(componentType)) {
            return Arrays.toString((byte[]) arrayOfPrimitives);
        } else if ("short".equals(componentType)) {
            return Arrays.toString((short[]) arrayOfPrimitives);
        } else if ("int".equals(componentType)) {
            return Arrays.toString((int[]) arrayOfPrimitives);
        } else if ("long".equals(componentType)) {
            return Arrays.toString((long[]) arrayOfPrimitives);
        } else if ("float".equals(componentType)) {
            return Arrays.toString((float[]) arrayOfPrimitives);
        } else if ("double".equals(componentType)) {
            return Arrays.toString((double[]) arrayOfPrimitives);
        } else if ("boolean".equals(componentType)) {
            return Arrays.toString((boolean[]) arrayOfPrimitives);
        } else {
            return Arrays.toString((char[]) arrayOfPrimitives);
        }
    }
}
