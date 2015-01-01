package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.*;
import org.junit.runner.*;

import junitparams.usage.person_example.*;

@RunWith(JUnitParamsRunner.class)
public class EnumsAsParamsTest {

    @Test
    @Parameters({"SOME_VALUE", "OTHER_VALUE"})
    public void passEnumAsString(PersonType person) {
        assertThat(person).isIn(PersonType.SOME_VALUE, PersonType.OTHER_VALUE);
    }

    @Test
    @Parameters
    public void passEnumFromMethod(PersonType person) {
        assertThat(person).isIn(parametersForPassEnumFromMethod());
    }

    private PersonType[] parametersForPassEnumFromMethod() {
        return new PersonType[] {PersonType.SOME_VALUE, PersonType.OTHER_VALUE};
    }
}
