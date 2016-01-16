package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;


@RunWith(JUnitParamsRunner.class)
public class RulesTest {
    private static final String DESCRIPTION_TEMPLATE = "[%s] %s (%s)(%s)";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    @Rule
    public ExpectedException exception = ExpectedException.none();
    @Rule
    public ErrorCollector errors = new ErrorCollector();
    @Rule
    public TestName testName = new TestName();
    @Rule
    public TestWatcher testWatcher = new TestWatcher() {
    };
    @Rule
    public Timeout timeout = new Timeout(0);
    @Rule
    public DescriptionRule description = new DescriptionRule();


    @Test
    @Parameters("")
    public void shouldHandleRulesProperly(String n) {
        assertThat(testName.getMethodName()).isEqualTo("[0]  (shouldHandleRulesProperly)");
    }

    @Test
    @Parameters({"0", "1"})
    public void rulesShouldReceiveCorrectDescription(String value) {
      assertThat(description.description.toString())
          .isEqualTo(String.format(
              DESCRIPTION_TEMPLATE, value, value, "rulesShouldReceiveCorrectDescription",
              RulesTest.class.getName()));
    }

    private static class DescriptionRule implements TestRule {
        private Description description;

        private void setDescription(Description description) {
            this.description = description;
        }

        @Override
        public Statement apply(final Statement base, final Description description) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                  setDescription(description);
                  base.evaluate();
                }
            };
        }
    }
}
