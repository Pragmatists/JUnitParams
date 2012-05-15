package junitparams;

import static junitparams.JUnitParamsRunner.*;
import static org.junit.Assert.*;

import java.util.*;

import junitparams.PersonTest.Person;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class CastingParamsTest {

    @Test
    @Parameters
    public void wrapWithTypeBySingleArgConstructor(Person person) {
        assertTrue(person.getAge() > 0);
    }

    List<Integer> parametersForWrapWithTypeBySingleArgConstructor() {
        return Arrays.asList(1, 2);
    }

    @Test
    @Parameters
    public void wrapWithTypeByMultiArgConstructor(Person person) {
        assertTrue(person.getAge() > 0);
    }

    Object parametersForWrapWithTypeByMultiArgConstructor() {
        return $($("first", 1), $("second", 2));
    }

    @Test
    @Parameters
    public void cartoonCharacters(Person character) {
    }

    private List<?> parametersForCartoonCharacters() {
        return Arrays.asList(
            $("Tarzan", 0),
            $("Jane", 20)
            );
    }
}
