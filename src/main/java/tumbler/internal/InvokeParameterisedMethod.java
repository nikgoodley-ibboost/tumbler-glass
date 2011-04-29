package tumbler.internal;

import org.junit.runners.model.*;

import tumbler.internal.domain.*;

public class InvokeParameterisedMethod extends Statement {

    private final Object[] params;
    private final FrameworkMethod testMethod;
    private final Object target;
    private final String paramsAsString;

    public String getParamsAsString() {
        return paramsAsString;
    }

    public InvokeParameterisedMethod(FrameworkMethod testMethod, Object target, String params) {
        this.testMethod = testMethod;
        this.target = target;
        paramsAsString = params;
        this.params = castParamsFromString(params);
    }

    private Object[] castParamsFromString(String params) {
        Object[] columns = TableRow.from(params).columns();
        Class<?>[] parameterTypes = testMethod.getMethod().getParameterTypes();
        
        verifySameSizeOfArrays(columns, parameterTypes);
        columns = castColumns(columns, parameterTypes);
        
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
            throw new IllegalArgumentException("Number of parameters inside @Params annotation doesn't match the number of scenario method parameters.\nThere are " + columns.length + " parameters in annotation, while there's " + parameterTypes.length + " parameters in the " + testMethod.getName() + " method.");
    }

    @Override
    public void evaluate() throws Throwable {
        testMethod.invokeExplosively(target, params);
    }
}
