package junitparams;

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
    @SuppressWarnings("deprecation")
    public TestWatchman testWatchman = new TestWatchman();
    @Rule
    public Timeout timeout = new Timeout(0);
    @Rule
    public Verifier verifier = new Verifier();

    @Test
    @Parameters("")
    public void shouldHandleRulesProperly() throws Exception {

    }
}
