package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class BeforeAfterClassTest {

    private static boolean val = false;

    @BeforeClass
    public static void before() {
        val = true;
    }

    @AfterClass
    public static void after() {
        val = false;
    }

    @Test
    @Parameters({ " " })
    public void test(String param) {
        assertThat(val).isTrue();
    }
}
