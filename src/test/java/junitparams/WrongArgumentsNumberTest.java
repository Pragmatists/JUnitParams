package junitparams;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class WrongArgumentsNumberTest {

    @Test(expected = IllegalArgumentException.class)
    @Parameters({"one"})
    public void throwsExceptionForWrongNumberOfParameters(String value, String notProvided) {
    }

}
