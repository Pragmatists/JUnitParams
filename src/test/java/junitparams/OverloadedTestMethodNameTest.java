package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(number).isIn(1, 2);
    }

    @Test
    @Parameters({ "1,2" })
    public void aTest(int number1, int number2) {
        assertThat(number1).isEqualTo(1);
        assertThat(number2).isEqualTo(2);
    }
}
