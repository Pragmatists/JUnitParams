package junitparams.internal;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A memoization container to compute the value once and only once in a thread
 * safe fashion.
 *
 * @param <T> the type to memoise
 */
abstract class Memoizer<T> {
    private final ReentrantReadWriteLock.ReadLock readLock;
    private final ReentrantReadWriteLock.WriteLock writeLock;

    private volatile T value;

    /**
     * Construct a new memoisation container.
     */
    Memoizer() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    /**
     * Get the memoized value. This will compute the value if the value has yet
     * to be set.
     *
     * @return the memoized value
     * @throws NullPointerException if {@link #computeValue()} returns null
     */
    public T get() {
        // check for existing value
        readLock.lock();
        try {
            if (value != null) {
                return value;
            }
        } finally {
            readLock.unlock();
        }

        // compute new value
        writeLock.lock();
        try {
            // double check the value
            if (value == null) {
                T newValue = computeValue();
                if (newValue == null) {
                    throw new NullPointerException("computeValue should never null");
                }
                value = newValue;
            }
            return value;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Compute the memoized value.
     *
     * @return the value
     */
    protected abstract T computeValue();
}
