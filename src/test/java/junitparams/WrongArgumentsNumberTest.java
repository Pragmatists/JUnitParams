package junitparams;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class WrongArgumentsNumberTest {

    @Test(expected = IllegalArgumentException.class)
    @Parameters({"one"})
    public void trows_exception_for_wrong_number_of_parameters(String value, String notProvided) {
    }

}
