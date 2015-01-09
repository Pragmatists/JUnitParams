package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import junitparams.usage.person_example.PersonTest;
import junitparams.usage.person_example.PersonTest.Person;

import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class MethodAnnotationArgumentTest {

    @Test
    @Parameters(method = "return1")
    public void testSingleMethodName(int number) {
        assertThat(number).isEqualTo(1);
    }

    private Integer[] return1() {
        return new Integer[] { 1 };
    }

    @Test
    @Parameters(method = "return1,return2")
    public void testMultipleMethodNames(int number) {
        assertThat(number)
                .isLessThanOrEqualTo(2)
                .isGreaterThanOrEqualTo(1);
    }

    @Test
    @Parameters(method = "return1, return2")
    public void testMultipleMethodNamesWithWhitespaces(int number) {
        assertThat(number)
                .isLessThanOrEqualTo(2)
                .isGreaterThanOrEqualTo(1);
    }

    private Integer[] return2() {
        return new Integer[] { 2 };
    }

    @Test
    @Parameters(source = PersonTest.class, method = "adultValues")
    public void testSingleMethodFromDifferentClass(int age, boolean valid) {
        assertThat(new Person(age).isAdult()).isEqualTo(valid);
    }

    @Test
    @Parameters(method = "stringParamsWithNull")
    public void shouldPassStringParamsWithNullFromMethod(String parameter) {
        // given
        List<String> acceptedParams = Arrays.asList("1", "2", "3", null);

        // then
        assertThat(acceptedParams).contains(parameter);
    }

    Object[] stringParamsWithNull() {
        return genericArray("1", "2", "3", null);
    }

    @Test
    @Parameters(method = "multiStringParams")
    public void shouldPassMultiStringParams(String first, String second) {
        assertThat(first).isEqualTo(second);
    }

    Object[] multiStringParams() {
        return genericArray(
                genericArray("test", "test"),
                genericArray("ble", "ble"));
    }

    private static <T> T[] genericArray(T... elements) {
        return elements;
    }
}
