package junitparams.missingparams;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.assertEquals;

public class MissingParametersTest {


    @RunWith(JUnitParamsRunner.class)
    public static class MissingParametersInMethodProvider {


        @Test
        @Parameters(method = "parametersProvider")
        public void missingParameters(String param1, String param2) {
            assertEquals(param1, param2);
        }

        public Object[][] parametersProvider() {
            return new Object[][]{
                    new Object[]{"pass", "pass"},
                    new Object[]{"fail"}
            };
        }
    }

    @Test
    public void missingParametersInMethodThrowsIllegalArgumentException() {
        Result testResult = JUnitCore.runClasses(MissingParametersInMethodProvider.class);
        Failure testFailure = testResult.getFailures().iterator().next();

        assertEquals(1, testResult.getFailureCount());
        assertEquals(IllegalArgumentException.class, testFailure.getException().getClass());
        assertEquals("Number of parameters in data provider method doesn't match the number of test method parameters.\n" +
                        "There are 1 parameters in provider method, while there's 2 parameters in the missingParameters method."
                , testFailure.getException().getMessage());
    }

    @RunWith(JUnitParamsRunner.class)
    public static class MissingNullParametersInMethodProvider {

        public MissingNullParametersInMethodProvider() {
        }

        @Test
        @Parameters(method = "withWrongNumberOfNullParams")
        public void testWithValueAndMethodProviders(String param1, String param2, String param3) {
            assertEquals(param1, param2, param3);
        }

        public Object[][] withWrongNumberOfNullParams() {
            return new Object[][]{
                    new Object[]{null, null}
            };
        }

    }

    @Test
    public void missingNullParametersInMethodThrowsIllegalArgumentException() {
        Result testResult = JUnitCore.runClasses(MissingNullParametersInMethodProvider.class);
        Failure testFailure = testResult.getFailures().iterator().next();

        assertEquals(1, testResult.getFailureCount());
        assertEquals(IllegalArgumentException.class, testFailure.getException().getClass());
        assertEquals("Number of parameters in data provider method doesn't match the number of test method parameters.\n" +
                        "There are 2 parameters in provider method, while there's 3 parameters in the testWithValueAndMethodProviders method.",
                testFailure.getException().getMessage());
    }


}



