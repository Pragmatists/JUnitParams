package junitparams.internal.parameters.toarray;

import java.util.ArrayList;
import java.util.List;

class IterableResultToArray implements ResultToArray {
    private Object result;

    IterableResultToArray(Object result) {
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

        try {
            List<Object[]> res = new ArrayList<Object[]>();
            for (Object[] paramSet : (Iterable<Object[]>) result)
                res.add(paramSet);
            return res.toArray();
        } catch (ClassCastException e1) {
            // Iterable with consecutive paramsets, each of one param
            List<Object> res = new ArrayList<Object>();
            for (Object param : (Iterable<?>) result)
                res.add(new Object[]{param});
            return res.toArray();
        }
    }
}
