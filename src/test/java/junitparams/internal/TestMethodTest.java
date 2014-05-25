package junitparams.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

import junitparams.*;

@RunWith(JUnitParamsRunner.class)
public class TestMethodTest {
    TestMethod plainTestMethod;
    TestMethod arrayTestMethod;

    @Before
    public void setUp() throws Exception {
        plainTestMethod = new TestMethod(new FrameworkMethod(TestMethodTest.class.getMethod("for_others_to_work", new Class[]{String.class})),
                new TestClass(this.getClass()));
        arrayTestMethod = new TestMethod(new FrameworkMethod(TestMethodTest.class.getMethod("for_others_to_work_with_array",
                new Class[]{(new String[]{}).getClass()})),
                new TestClass(this.getClass()));
    }

    @Test
    @Parameters({"a","b"})
    public void for_others_to_work(String arg) throws Exception {
    }


    @Test
    @Parameters({"a,b","b,a"})
    public void for_others_to_work_with_array(String... arg) throws Exception {
    }

    @Test
    @Ignore
    public void flatTestMethodStructure() throws Exception {
        System.setProperty("JUnitParams.flat", "true");

        Description description = plainTestMethod.describe();

        assertEquals("for_others_to_work(junitparams.internal.TestMethodTest)", description.getDisplayName());
        assertTrue(description.getChildren().isEmpty());
        System.clearProperty("JUnitParams.flat");
    }


    @Test
    public void hierarchicalTestMethodStructure() throws Exception {
        Description description = plainTestMethod.describe();

        assertEquals("for_others_to_work", description.getDisplayName());
        assertEquals("[0] a (for_others_to_work)(junitparams.internal.TestMethodTest)", description.getChildren().get(0).getDisplayName());
        assertEquals("[1] b (for_others_to_work)(junitparams.internal.TestMethodTest)", description.getChildren().get(1).getDisplayName());
    }

    @Test
    public void hierarchicalArrayTestMethodStructure() throws Exception {
        Description description = arrayTestMethod.describe();

        assertEquals("for_others_to_work_with_array", description.getDisplayName());
        assertEquals("[0] a,b (for_others_to_work_with_array)(junitparams.internal.TestMethodTest)",
                description.getChildren().get(0).getDisplayName());
        assertEquals("[1] b,a (for_others_to_work_with_array)(junitparams.internal.TestMethodTest)",
                description.getChildren().get(1).getDisplayName());
    }
}
