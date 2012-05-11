package junitparams;

import static org.junit.Assert.*;
import junitparams.PersonTest.Person;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class FileParamsTest {

    @Test
    @FileParameters("src/test/resources/test.csv")
    public void loadParamsFromFileWithIdentityMapper(int age, String name) {
        assertTrue(age > 0);
    }

    @Test
    @FileParameters(value = "src/test/resources/test.csv", mapper = PersonMapper.class)
    public void loadParamsFromFileWithCustomMapper(Person person) {
        assertTrue(person.getAge() > 0);
    }
}
