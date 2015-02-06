package junitparams;

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
        return new Object[]{new Object[]{"first", 1}, new Object[]{"second", 2}};
    }

    @Test
    @Parameters
    public void cartoonCharacters(PersonTest.Person character) {
        assertThat(character.getName()).isIn("Tarzan", "Jane");
        assertThat(character.getAge()).isIn(0, 20);
    }

    private List<?> parametersForCartoonCharacters() {
        return Arrays.asList(
                new Object[]{"Tarzan", 0},
                new Object[]{"Jane", 20}
            );
    }

    @Test
    @Parameters(method = "strings")
    public void stringArrayFromMethod(String... values) {
        assertThat(values).containsOnlyOnce("1", "2");
    }
    private Object strings() {
        return new Object[]{new Object[]{"1", "2"}, new Object[]{"2", "1"}};
    }

    @Test
    @Parameters({"a,b", "b,a"})
    public void stringArrayFromAnnotation(String... values) {
        assertThat(values).containsOnlyOnce("a","b");
    }

}
