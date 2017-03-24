package junitparams.internal.parameters.toarray;

import java.util.ArrayList;
import java.util.List;

class SimpleIterableResultToArray implements ResultToArray {
    private final Object result;

    SimpleIterableResultToArray(Object result) {
        this.result = result;
    }

    @Override
    public boolean isApplicable() {
        return Iterable.class.isAssignableFrom(result.getClass());
    }

    @Override
    public Object[] convert() {
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
