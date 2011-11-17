package junitparams;

import static junitparams.JUnitParamsRunner.$;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import junitparams.PersonTest.Person;

import org.junit.*;
import org.junit.runner.*;

public class SubclassTest extends SuperclassTest {

    @Test
    @Parameters(method = "paramsForIsAdult")
    public void isAdult(int age, boolean valid) throws Exception {
        assertThat(new Person(age).isAdult(), is(valid));
    }
    
    private Object[] paramForSuperclassMethod() {
		return $(1);
	}
}