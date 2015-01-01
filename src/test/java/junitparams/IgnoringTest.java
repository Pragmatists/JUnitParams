package junitparams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.assertj.core.api.Assertions;
import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class IgnoringTest {

    @Test
    @Ignore
    public void ignoreMeNoParams() {
        fail();
    }

    @Test
    @Parameters("")
    @Ignore
    public void ignoreMeWithParams() {
        fail();
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
    @Parameters(method = "noMethod")
    public void shouldNotTryToInvokeMethodWhenTestIgnored(Object a) {
        fail();
    }
}
