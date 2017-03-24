package junitparams.internal;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.fail;
import static org.junit.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class TestMethodMemorisationTest {
    private AtomicInteger counter;
    private TestMethod testMethod;

    @Before
    public void setUp() throws Exception {
        counter = new AtomicInteger();
        Method method = TestMethodMemorisationTest.class
                .getMethod("sampleTestMethod", String.class);

        testMethod = new TestMethod(
                new FrameworkMethod(method),
                new TestClass(this.getClass())
        ) {
            @Override
            Description computeDescription() {
                counter.incrementAndGet();
                return super.computeDescription();
            }
        };
    }

    @Test
    public void testComputationOccursOnce() throws Exception {
        for (int i = 0; i < 100; i++) {
            testMethod.describe();
        }
        assertEquals(1, counter.get());
    }

    @Parameters({"SNARK"})
    public void sampleTestMethod(String arg) {
        fail("Unexpected invocation");
    }
}
