package junitparams;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

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
    @Parameters({" "})
    public void test(String param) {
        assertThat(val).isTrue();
    }


    @Test
    @Parameters({
            "junitparams.BeforeAfterClassTest$NonStaticBeforeTest",
            "junitparams.BeforeAfterClassTest$NonStaticBeforeTest"
    })
    public void shouldProvideHelpfulExceptionMessageWhenLifecycleAnnotationUsedImproperly(Class<?> testClass) {
        Result result = JUnitCore.runClasses(testClass);

        assertThat(result.getFailureCount()).isEqualTo(1);
        assertThat(result.getFailures().get(0).getException())
                .hasMessage("Method fail() should be static");
    }


    @RunWith(JUnitParamsRunner.class)
    private static class NonStaticBeforeTest {

        @BeforeClass
        public void fail() {
        }

        @Test
        public void test() {
        }
    }

    @RunWith(JUnitParamsRunner.class)
    private static class NonStaticAfterTest {

        @AfterClass
        public void fail() {
        }

        @Test
        public void test() {
        }
    }


}
