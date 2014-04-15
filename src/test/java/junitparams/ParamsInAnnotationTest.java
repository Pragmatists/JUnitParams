package junitparams;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class ParamsInAnnotationTest {

    @Test
    @Parameters({ "1", "2" })
    public void singleParam(int number) {
        assertTrue(number > 0);
    }

    @Test
    @Parameters({ "a \n b", "a(asdf)", "a \r a" })
    public void specialCharsInParam(String a) throws Exception {
    }

    @Test
    @Parameters({ "1, false", "2, true" })
    public void multipleParams(int number, boolean what) throws Exception {
        assertEquals(what, number > 1);
    }

    @Test
    @Parameters({ ",1"})
    public void emptyFirstParam(String empty, int number) {
        assertEquals("", empty);
        assertEquals(1, number);
    }

    @Test
    @Parameters({ "1,"})
    public void emptyLastParam(int number, String empty) {
        assertEquals(1, number);
        assertEquals("", empty);
    }

    @Test
    @Parameters({ "1,,1"})
    public void emptyMiddleParam(int number1, String empty, int number2) {
        assertEquals(1, number1);
        assertEquals("", empty);
        assertEquals(1, number2);
    }

    @Test
    @Parameters({","})
    public void allParamsEmpty(String empty1, String empty2) {
        assertEquals("", empty1);
        assertEquals("", empty2);
    }
}
