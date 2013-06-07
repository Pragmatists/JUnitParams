package junitparams;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;

import junitparams.usage.person_example.*;
import junitparams.usage.person_example.PersonTest.*;

@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class MethodAnnotationArgumentTest {

    @Test
    @Parameters(method = "return1")
    public void testSingleMethodName(int number) {
        assertEquals(1, number);
    }

    private Integer[] return1() {
        return new Integer[] {1};
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

    private Integer[] return2() {
        return new Integer[] {2};
    }

    @Test
    @Parameters(source = PersonTest.class, method = "adultValues")
    public void testSingleMethodFromDifferentClass(int age, boolean valid) {
        assertThat(new Person(age).isAdult(), is(valid));
    }
}
