package junitparams;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

public class NotSpecifiedParameters {

    @Test
    public void failsWhenNoParamsProvided() throws Exception {
        Result result = JUnitCore.runClasses(Subject.class);

        assertThat(result.wasSuccessful()).isFalse();

        Failure testFailure = result.getFailures().iterator().next();
        assertThat(testFailure.getException()).isInstanceOf(IllegalStateException.class);
        assertThat(testFailure.getMessage()).contains("methodWithoutParameters");
    }

    @RunWith(JUnitParamsRunner.class)
    class Subject {

        @Test
        @Parameters
        public void methodWithoutParameters(String param) throws Exception {

        }
    }
}
