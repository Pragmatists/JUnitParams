package junitparams;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class IgnoringTest {
    @Test
    @Ignore
    public void ignoreMeNoParams() {

    }

    @Test
    @Parameters("")
    @Ignore
    public void ignoreMeWithParams() {

    }

    @Test
    public void dontIgnoreMeNoParams() {

    }

    @Test
    @Parameters("")
    public void dontIgnoreMeWithParams() {

    }
}
