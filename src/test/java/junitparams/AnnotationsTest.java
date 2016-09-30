package junitparams;

import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class AnnotationsTest {

    public class CustomListener extends RunListener {
        @Override
        public void testStarted(Description description) throws Exception {
            assertThat(description.getAnnotation(CustomAnnotationsTest.CustomAnnotation.class), notNullValue());
        }
    }

    @Test
    public void parameterizedMethodShouldHaveAnnotations() {
        JUnitCore core = new JUnitCore();
        core.addListener(new TextListener(System.out));
        core.addListener(new CustomListener());
        Result result = core.run(CustomAnnotationsTest.class);
        assertThat("Two tests should start", result.getRunCount(), is(2));
        assertThat("One test should fail", result.getFailureCount(), is(1));
    }

}
