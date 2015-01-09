package junitparams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class IgnoringTest {

    @Test
    @Ignore
    public void ignoreMeNoParams() {
        fail("Should be ignored");
    }

    @Test
    @Ignore
    @Parameters("")
    public void ignoreMeWithParams() {
        fail("Should be ignored");
    }

    @Test
    public void dontIgnoreMeNoParams() {
    }

    @Test
    @Parameters("")
    public void dontIgnoreMeWithParams(String a) {
        assertThat(a).isEqualTo("");
    }

    @Test
    @Ignore
    @Parameters(method = "someMethod")
    public void shouldNotTryToInvokeMethodWhenTestIgnored(Object a) {
        fail("Should be ignored");
    }

    private Object[] someMethod() {
        fail("Should not be called");
        return null;
    }
}
