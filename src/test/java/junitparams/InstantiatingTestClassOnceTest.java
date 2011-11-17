package junitparams;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertThat;

@RunWith(JUnitParamsRunner.class)
public class InstantiatingTestClassOnceTest {

    private static final int NUMBER_OF_TEST_METHODS_IN_THIS_CLASS = 1;
    private static int instantiationsCount = 0;

    @Test
    public void shouldBeInstantiatedOncePerTestMethod() {
        assertThat(instantiationsCount, is(NUMBER_OF_TEST_METHODS_IN_THIS_CLASS));
    }

    public InstantiatingTestClassOnceTest() {
        instantiationsCount++;
    }

}