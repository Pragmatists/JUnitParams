package junitparams.internal.parameters.toarray;

import junitparams.internal.parameters.ParameterTypeSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

class ObjectArrayResultToArray implements ResultToArray {
    private Object result;
    private ParameterTypeSupplier parameterTypeSupplier;

    ObjectArrayResultToArray(Object result, ParameterTypeSupplier parameterTypeSupplier) {
        this.result = checkNotNull(result, "result must not be null");
        this.parameterTypeSupplier = checkNotNull(parameterTypeSupplier, "parameterTypeSupplier must not be null");
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
        if (parameterTypeSupplier.getParameterTypes().size() != params.length) {
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
