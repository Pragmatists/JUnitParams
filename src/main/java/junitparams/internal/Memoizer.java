package junitparams.internal;

abstract class Memoizer<T> {

    private volatile T value;

    public T get() {
        if (value != null) {
            return value;
        }
        value = computeValue();
        return value;

    }

    protected abstract T computeValue();
}
