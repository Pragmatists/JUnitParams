package junitparams;

import static junitparams.JUnitParamsRunner.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class NullValuesTest {

    @Test
    @Parameters(method = "singleParams")
    public void passesForSingleParametersListWithTheWorkaround(Object param) { }

    public Object[] singleParams() {
        return $(
            new Object[] { null }, // the workaround
            "string",
            Long.valueOf(1));
    }

    @Test
    @Parameters(method = "multipleParams")
    public void passesForMultipleParametersOutOfBox(Object param1, Object param2, Object param3) { }

    public Object[] multipleParams() {
        return $(
            $(null, new Object(), null),
            $(new Object(), new Object(), null),
            $(null, null, null));
    }

}
