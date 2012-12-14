package junitparams;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class OverloadedTestMethodNameTest {

    @Test
    public void aTest() {
    }

    @Test
    @Parameters({ "1", "2" })
    public void aTest(int number) {
    }

    @Test
    @Parameters({ "1,2" })
    public void aTest(int number1, int number2) {
    }
}
