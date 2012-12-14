package junitparams;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class AssumptionsWithParamsTest {

    @Test
    @Parameters({ "true", "false" })
    public void assumeOnceWorksAndOnceIgnores(boolean value) {
        Assume.assumeTrue(value);
        assertTrue(value);
    }
}
