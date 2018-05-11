package junitparams.internal;

import junitparams.JUnitParamsRunner;
import org.assertj.core.api.iterable.Extractor;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class JUnitParamsRunnerTest {

    private static final RuntimeException TEST_EXCEPTION = new RuntimeException("Test Exception");

    @Test
    public void shouldGetOriginalExceptionThrownBySampleTestConstructor() {
        final Result testResult = JUnitCore.runClasses(ExceptionInSampleTestConstructorTest.class);
        final Extractor<Failure, Throwable> throwableProperty = new Extractor<Failure, Throwable>() {
            @Override
            public Throwable extract(Failure failure) {
                return failure.getException();
            }
        };
        assertThat(testResult.getFailures())
                .extracting(throwableProperty)
                .contains(TEST_EXCEPTION);
    }

    @RunWith(JUnitParamsRunner.class)
    public static class ExceptionInSampleTestConstructorTest {

        @SuppressWarnings("WeakerAccess")
        public ExceptionInSampleTestConstructorTest() {
            throw TEST_EXCEPTION;
        }

        @Test
        @SuppressWarnings({"unused", "WeakerAccess"})
        public void testCase() {
            fail("Should not be invoked");
        }

    }
}
