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
    public TestWatcher testWatcher = new TestWatcher() {
    };
    @Rule
    public Timeout timeout = new Timeout(0);

    @Test
    @Parameters("")
    public void shouldHandleRulesProperly() throws Exception {

    }
}
