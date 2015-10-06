package junitparams;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.*;

import junitparams.usage.person_example.PersonTest.*;

public class SubclassTest extends SuperclassTest {

    @Test
    @Parameters(method = "paramsForIsAdult")
    public void isAdult(int age, boolean valid) throws Exception {
        assertThat(new Person(age).isAdult(), is(valid));
    }

    @SuppressWarnings("unused")
    private Object[] paramsForSuperclassMethod() {
        return new Object[]{1};
    }
}