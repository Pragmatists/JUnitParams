package junitparams;

import org.junit.*;
import org.junit.runner.*;

import junitparams.usage.person_example.*;

@RunWith(JUnitParamsRunner.class)
public class EnumsAsParamsTest {

    @Test
    @Parameters({"SOME_VALUE", "OTHER_VALUE"})
    public void passEnumAsString(PersonType person) {
    }

    @Test
    @Parameters
    public void passEnumFromMethod(PersonType person) {
    }

    private PersonType[] parametersForPassEnumFromMethod() {
        return (PersonType[]) new PersonType[] {PersonType.SOME_VALUE, PersonType.SOME_VALUE};
    }
}
