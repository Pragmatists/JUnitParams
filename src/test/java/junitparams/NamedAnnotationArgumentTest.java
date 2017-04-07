package junitparams;

import junitparams.usage.person_example.PersonTest;
import junitparams.usage.person_example.PersonTest.Person;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class NamedAnnotationArgumentTest {

    @Test
    @Parameters(named = "return1")
    public void testSingleNamedMethod(int number) {
        assertThat(number).isEqualTo(1);
    }

    @Named("return1")
    private Integer[] returnNumberOne() {
        return new Integer[] { 1 };
    }

    @Test
    @Parameters(named = "return1,return2")
    public void testMultipleNamedMethods(int number) {
        assertThat(number)
                .isLessThanOrEqualTo(2)
                .isGreaterThanOrEqualTo(1);
    }

    @Test
    @Parameters(named = "return1, return2")
    public void testMultipleNamedMethodsWithWhitespaces(int number) {
        assertThat(number)
                .isLessThanOrEqualTo(2)
                .isGreaterThanOrEqualTo(1);
    }

    @Named("return2")
    private Integer[] returnNumberTwo() {
        return new Integer[] { 2 };
    }

    @Test
    @Parameters(source = PersonTest.class, named = "grownups")
    public void testSingleMethodFromDifferentClass(int age, boolean valid) {
        assertThat(new Person(age).isAdult()).isEqualTo(valid);
    }

    @Test
    @Parameters(named = "stringParamsWithNull")
    public void shouldPassStringParamsWithNullFromMethod(String parameter) {
        List<String> acceptedParams = Arrays.asList("1", "2", "3", null);

        assertThat(acceptedParams).contains(parameter);
    }

    @Named("stringParamsWithNull")
    Object[] stringWithNullParameters() {
        return genericArray("1", "2", "3", null);
    }

    @Test
    @Parameters(named = "multiStringParams")
    public void shouldPassMultiStringParams(String first, String second) {
        assertThat(first).isEqualTo(second);
    }

    @Named("multiStringParams")
    Object[] multiStringParameters() {
        return genericArray(
                genericArray("test", "test"),
                genericArray("ble", "ble"));
    }

    private static <T> T[] genericArray(T... elements) {
        return elements;
    }
}
