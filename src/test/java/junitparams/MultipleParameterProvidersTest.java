/**
 * @copyright 2006-2015 Bronto Software, Inc.
 */
package junitparams;

import junit.framework.JUnit4TestAdapter;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junitparams.internal.parameters.ParametersReader;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

import static java.lang.String.format;

public class MultipleParameterProvidersTest {

    private JUnit4TestAdapter adapter;

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
        adapter = new JUnit4TestAdapter(CantInitializeWithValueAndMethodProvider.class);
        TestResult testResult = new TestResult();
        adapter.run(testResult);

        TestFailure error = testResult.errors().nextElement();

        assertEquals(1, testResult.errorCount());
        assertEquals(IllegalStateException.class, error.thrownException().getClass());
        assertEquals(format(ParametersReader.ILLEGAL_STATE_EXCEPTION_MESSAGE, "testWithValueAndMethodProviders"),
                     error.exceptionMessage());
    }
}
