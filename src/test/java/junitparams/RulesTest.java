package junitparams;

import junitparams.naming.TestCaseName;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.rules.Timeout;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;

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
    @TestCaseName("{method}")
    public void shouldHandleRulesProperly(String n) {
        assertThat(testName.getMethodName()).isEqualTo("shouldHandleRulesProperly");
    }

    @Test
    public void shouldProvideHelpfulExceptionMessageWhenRuleIsUsedImproperly() {
        Result result = JUnitCore.runClasses(ProtectedRuleTest.class);

        assertThat(result.getFailureCount()).isEqualTo(1);
        assertThat(result.getFailures().get(0).getException())
                .hasMessageContaining("The @Rule 'testRule' must be public.");
    }

    public class ProtectedRuleTest {
        @Rule
        TestRule testRule;

        @Test
        public void test() {

        }
    }

}
