package junitparams;

import static junitparams.JUnitParamsRunner.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;

@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class ParametersForMethodTest {
    @Test
    @Parameters
    public void oneParamDifferentTypes(int number, String a) {
        assertTrue(number > 0);
    }

    private Object[] parametersForOneParamDifferentTypes() {
        return $($(1, "a"));
    }

    @Test
    @Parameters
    public void oneParamSetOneNull(String a, String b) {
    }

    private Object[] parametersForOneParamSetOneNull() {
        return $($(null, "b"));
    }

    @Test
    @Parameters
    public void noToString(NoToStringObject o) {
    }

    private Object[] parametersForNoToString() {
        return $($(new NoToStringObject()));
    }

    public class NoToStringObject {
    }

    @Test
    @Parameters
    public void shouldIgnoreWhenEmptyParamset() {
    }

    private Object[] parametersForShouldIgnoreWhenEmptyParamset() {
        return new Object[] {};
    }

    @Test
    @Parameters({ "a \n \\,\\|b", "\\,a(asdf)\\|", "\\,", "", "\r\n" })
    public void escapedSpecialCharsInParam(String a) {

    }
}
