package junitparams;

import junitparams.internal.parameters.ParametersReader;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.assertEquals;

import static java.lang.String.format;

public class MultipleParameterProvidersTest {

    @RunWith(JUnitParamsRunner.class)
    private class CantInitializeWithValueAndMethodProvider {
        private Object[] extraProvider() {
            return new Object[][]{
                {"test", "test"},
            };
        }

        @Parameters(value = {"test, test"}, method = "extraProvider")
        @Test
        public void testWithValueAndMethodProviders(String input, String output) {
            assertEquals(input, output);
        }
    }

    @Test
    public void testWithValueAndMethodProvidersThrowsIllegalStateException() {
        Result testResult = JUnitCore.runClasses(CantInitializeWithValueAndMethodProvider.class);
        assertEquals(1, testResult.getFailureCount());

        Failure testFailure = testResult.getFailures().iterator().next();
        assertEquals(IllegalStateException.class, testFailure.getException().getClass());
        assertEquals(format(ParametersReader.ILLEGAL_STATE_EXCEPTION_MESSAGE, "testWithValueAndMethodProviders"),
                     testFailure.getMessage());
    }
}
