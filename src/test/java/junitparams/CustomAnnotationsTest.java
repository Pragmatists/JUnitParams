package junitparams;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.junit.Assert.assertTrue;

/**
 * Created by artkoshelev on 30.09.16.
 */
@RunWith(JUnitParamsRunner.class)
public class CustomAnnotationsTest {

    @Retention(RUNTIME)
    public @interface CustomAnnotation {

    }

    @Test
    @Parameters({ "true", "false" })
    @CustomAnnotation
    public void testParams(boolean value) {
        assertTrue(value);
    }
}
