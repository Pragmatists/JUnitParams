package junitparams.internal.parameters.toarray;

import java.util.ArrayList;
import java.util.List;

class IterableResultToArray extends SimpleIterableResultToArray {
    private Object result;

    IterableResultToArray(Object result) {
        super(result);

        this.result = result;
    }

    @Override
    public boolean isApplicable() {
        return Iterable.class.isAssignableFrom(result.getClass());
    }

    @Override
    public Object[] convert() {
        try {
            // Attempt to handle Iterable<Iterable<Object>> case
            List<Object[]> res = new ArrayList<Object[]>();
            for (Iterable<Object> paramSetIterable : (Iterable<Iterable<Object>>) result) {
                List<Object> paramSet = new ArrayList<Object>();
                for (Object param : paramSetIterable) {
                    paramSet.add(param);
                }
                res.add(paramSet.toArray());
            }
            return res.toArray();
        } catch (ClassCastException e1) {
            // Eat the exception and keep trying
        }

        return super.convert();
    }
}
