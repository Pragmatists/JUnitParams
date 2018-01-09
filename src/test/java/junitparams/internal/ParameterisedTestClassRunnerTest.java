package junitparams.internal;

import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import static org.assertj.core.api.Assertions.assertThat;

public class ParameterisedTestClassRunnerTest {

    @Test
    public void shouldGetNullDescriptionForMethodNotYetInvoked() throws Exception {
        ParameterisedTestClassRunner runner = new ParameterisedTestClassRunner(new TestClass(SampleTest.class));
        runner.returnListOfMethods();

        Description test = runner.getDescriptionFor(new FrameworkMethod(SampleTest.class.getMethod("parametrized", String.class)));

        assertThat(test).isNull();
    }

    static class SampleTest {
        @Test
        @Parameters({ "123", "456" })
        @SuppressWarnings({"unused", "WeakerAccess"})
        public void parametrized(String param) {
        }
    }
}