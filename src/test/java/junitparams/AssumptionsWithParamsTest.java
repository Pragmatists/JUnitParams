package junitparams;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class AssumptionsWithParamsTest {

    @Test
    @Parameters({ "true", "false" })
    public void assumeOnceWorksAndOnceIgnores(boolean value) {
        assumeTrue(value);
        assertTrue(value);
    }
}
