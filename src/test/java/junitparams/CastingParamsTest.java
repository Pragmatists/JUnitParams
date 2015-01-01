package junitparams;

import static junitparams.JUnitParamsRunner.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import junitparams.usage.person_example.*;

@RunWith(JUnitParamsRunner.class)
public class CastingParamsTest {

    @Test
    @Parameters
    public void wrapWithTypeBySingleArgConstructor(PersonTest.Person person) {
        assertThat(person.getAge()).isGreaterThan(0);
    }
    List<Integer> parametersForWrapWithTypeBySingleArgConstructor() {
        return Arrays.asList(1, 2);
    }

    @Test
    @Parameters
    public void wrapWithTypeByMultiArgConstructor(PersonTest.Person person) {
        assertThat(person.getAge()).isGreaterThan(0);
    }
    Object parametersForWrapWithTypeByMultiArgConstructor() {
        return $($("first", 1), $("second", 2));
    }

    @Test
    @Parameters
    public void cartoonCharacters(PersonTest.Person character) {
        assertThat(character.getName()).isIn("Tarzan", "Jane");
        assertThat(character.getAge()).isIn(0, 20);
    }
    private List<?> parametersForCartoonCharacters() {
        return Arrays.asList(
            $("Tarzan", 0),
            $("Jane", 20)
            );
    }

    @Parameters(method = "strings")
    @Test
    public void stringArrayFromMethod(String... values) {
        assertThat(values).containsOnlyOnce("1", "2");
    }
    private Object strings() { return $($("1","2"), $("2","1")); }


    @Parameters({"a,b", "b,a"})
    @Test
    public void stringArrayFromAnnotation(String... values) {
        assertThat(values).containsOnlyOnce("a","b");
    }

}
