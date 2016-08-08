package junitparams.internal.parameters.toarray;

import org.junit.runners.model.FrameworkMethod;

class ObjectArrayResultToArray implements ResultToArray {
    private Object result;
    private FrameworkMethod frameworkMethod;

    ObjectArrayResultToArray(Object result, FrameworkMethod frameworkMethod) {
        this.result = result;
        this.frameworkMethod = frameworkMethod;
    }

    @Override
    public boolean isApplicable() {
        return Object[].class.isAssignableFrom(result.getClass());
    }

    @Override
    public Object[] convert() {
        return encapsulateParamsIntoArrayIfSingleParamsetPassed((Object[]) result);
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
