package junitparams.internal.parameters.toarray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class IteratorResultToArray implements ResultToArray {
    private Object result;

    IteratorResultToArray(Object result) {
        this.result = result;
    }

    @Override
    public boolean isApplicable() {
        return Iterator.class.isAssignableFrom(result.getClass());
    }

    @Override
    public Object[] convert() {
        Object iteratedElement = null;
        try {
            List<Object[]> res = new ArrayList<Object[]>();
            Iterator<Object[]> iterator = (Iterator<Object[]>) result;
            while (iterator.hasNext()) {
                iteratedElement = iterator.next();
                // ClassCastException will occur in the following line
                // if the iterator is actually Iterator<Object> in Java 7
                res.add((Object[]) iteratedElement);
            }
            return res.toArray();
        } catch (ClassCastException e1) {
            // Iterator with consecutive paramsets, each of one param
            List<Object> res = new ArrayList<Object>();
            Iterator<?> iterator = (Iterator<?>) result;
            // The first element is already stored in iteratedElement
            res.add(iteratedElement);
            while (iterator.hasNext()) {
                res.add(new Object[]{iterator.next()});
            }
            return res.toArray();
        }

    }
}
