package junitparams;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.*;
import org.junit.runner.*;

@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class ParametersReaderForMethodTest {

    @Test
    @Parameters
    public void oneParamDifferentTypes(int number, String a) {
        assertThat(number).isEqualTo(1);
        assertThat(a).isEqualTo("a");
    }

    private Object[] parametersForOneParamDifferentTypes() {
        return new Object[]{1, "a"};
    }

    @Test
    @Parameters
    public void oneParamSetOneNull(String a, String b) {
        assertThat(a).isNull();
        assertThat(b).isEqualTo("b");
    }

    private Object[] parametersForOneParamSetOneNull() {
        return new Object[]{null, "b"};
    }

    @Test
    @Parameters
    public void noToString(NoToStringObject o) {
        assertThat(o).isNotNull();
    }

    private Object[] parametersForNoToString() {
        return new Object[]{new NoToStringObject()};
    }

    public class NoToStringObject {
    }

    @Test
    @Parameters
    public void shouldIgnoreWhenEmptyParamset() {
        fail();
    }

    private Object[] parametersForShouldIgnoreWhenEmptyParamset() {
        return new Object[] {};
    }

    @Test
    @Parameters({ "a \n \\,\\|b", "\\,a(asdf)\\|", "\\,", "", "\r\n" })
    public void escapedSpecialCharsInParam(String a) {
        assertThat(a).
                isIn("a \n ,|b", ",a(asdf)|", ",", "").
                isNotIn("a \n \\,\\|b", "\\,a(asdf)\\|", "\\,", "\r\n");
    }
}
