package junitparams.nullcases.failing;

import static org.junit.Assert.*;
import junitparams.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class SingleParameterListNotUsingDollarTest {

    @Test
    @Parameters
    public void failsForSingleParams(Object param) {
        assertTrue(true);
    }

    public Object[] parametersForFailsForSingleParams() {
        return new Object[] {
                null, // causes NPE at
                      // junitparams.internal.Utils.asCsvString(Utils.java:39)
                "string",
                Long.valueOf(1)
        };
    }
}
