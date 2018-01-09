package junitparams.rules;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class TestWatcherDescriptionRuleTest {

    @Rule
    public final MethodNameTestWatcher rule = new MethodNameTestWatcher();

    @Test
    @Parameters({"AAA|1", "BBB|2"})
    @TestCaseName("p1_{0}_p2_{1}")
    public void paramsInAnnotationPipeSeparated(String p1, Integer p2) {
        rule.expectedTestName(format("p1_%s_p2_%s", p1, p2));
    }

    public static class MethodNameTestWatcher extends TestWatcher {

        private String expectedTestName;

        @Override
        public void finished(Description description) {
            assertThat(description.getMethodName()).isEqualTo(expectedTestName);
        }

        void expectedTestName(String expectedTestName) {
            this.expectedTestName = expectedTestName;
        }
    }
}
