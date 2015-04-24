package junitparams;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
public class ClassesAsParamsTest {

    @Test
    @Parameters({"java.lang.Object", "java.lang.String"})
    public void passClassAsString(Class<?> clazz) {
        assertThat(clazz).isIn(java.lang.Object.class, java.lang.String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    @Parameters("no.package.NoSuchClass")
    public void illegalArgumentExceptionWhenClassNotFound(Class<?> clazz) {
        fail("Should fail on test invocation");
    }
}
