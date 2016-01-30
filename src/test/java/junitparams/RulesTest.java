package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.*;
import org.junit.rules.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class RulesTest {
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


    @Test
    @Parameters("")
    public void shouldHandleRulesProperly(String n) {
        assertThat(testName.getMethodName()).isEqualTo("shouldHandleRulesProperly");
    }

    @Test
    public void shouldProvideHelpfulExceptionMessageWhenRuleIsUsedImproperly() {
        Result result = JUnitCore.runClasses(ProtectedRuleTest.class);
        assertThat(result.getFailureCount()).isEqualTo(1);

        String exceptionMessage = result.getFailures().get(0).getException().getMessage();
        assertThat(exceptionMessage).isEqualTo("The @Rule 'testRule' must be public.");
    }

    public class ProtectedRuleTest {
        @Rule
        TestRule testRule;

        @Test
        public void test() {

        }
    }

}
