package junitparams;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assume.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class AssumptionsTest {

    @Test
    @Parameters({ "true", "false" })
    public void assumeOnceWorksAndOnceIgnores(boolean value) {
        assumeThat(value, is(true));
        assertThat(value).isTrue();
    }
}
