package junitparams;

import static junitparams.JUnitParamsRunner.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;

@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class MethodAnnotationArgumentTest {

    @Test
    @Parameters(method = "return1")
    public void testSingleMethodName(int number) {
        assertEquals(1, number);
    }

    private Object[] return1() {
        return $($(1));
    }

    @Test
    @Parameters(method = "return1,return2")
    public void testMultipleMethodNames(int number) {
        assertTrue(2 >= number && number >= 1);
    }

    @Test
    @Parameters(method = "return1, return2")
    public void testMultipleMethodNamesWithWhitespaces(int number) {
        assertTrue(2 >= number && number >= 1);
    }

    private Object[] return2() {
        return $($(2));
    }
}
