package junitparams.internal;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.Collections.synchronizedList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link Memoizer}.
 */
public class MemoizerTest {
    private static void awaitUninterruptibly(CyclicBarrier barrier)
            throws BrokenBarrierException {
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    barrier.await();
                    return;
                } catch (InterruptedException e) {
                    interrupted = true;
                }
            }
        } finally {
            if (interrupted) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private AtomicInteger counter;
    private Memoizer<String> memoizer;

    @Before
    public void setUp() throws Exception {
        counter = new AtomicInteger();
        memoizer = new Memoizer<String>() {
            @Override
            protected String computeValue() {
                counter.incrementAndGet();
                return "SNARK";
            }
        };
    }

    @Test
    public void get() throws Exception {
        for (int i = 0; i < 100; i++) {
            assertEquals("SNARK", memoizer.get());
            assertEquals(1, counter.get());
        }
    }

    @Test
    public void getNullComputeValue() throws Exception {
        try {
            new Memoizer<String>() {
                @Override
                protected String computeValue() {
                    return null;
                }
            }.get();
            failBecauseExceptionWasNotThrown(NullPointerException.class);
        } catch (NullPointerException npe) {
            assertThat(npe)
                    .hasMessage("computeValue should never null");
        }
    }

    @Test
    public void getThreaded() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        final List<String> results = synchronizedList(new ArrayList<String>());
        final CyclicBarrier barrier = new CyclicBarrier(100);
        for (int i = 0; i < 100; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        awaitUninterruptibly(barrier);
                    } catch (BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                    results.add(memoizer.get());
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        assertThat(results)
                .hasSize(100)
                .containsOnly("SNARK");

        assertEquals(1, counter.get());
    }
}
