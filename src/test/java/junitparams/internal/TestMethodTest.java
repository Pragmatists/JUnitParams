package junitparams.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

import junitparams.*;

@RunWith(JUnitParamsRunner.class)
public class TestMethodTest {
    TestMethod testMethod;

    @Before
    public void setUp() throws Exception {
        testMethod = new TestMethod(new FrameworkMethod(TestMethodTest.class.getMethod("for_others_to_work", new Class[]{String.class})),
                new TestClass(this.getClass()));
    }

    @Test
    @Parameters({"a","b"})
    public void for_others_to_work(String arg) throws Exception {
    }

    @Test
    public void flatTestMethodStructure() throws Exception {
        System.setProperty("JUnitParams.flat", "true");

        Description description = testMethod.describe();

        assertEquals("for_others_to_work(junitparams.internal.TestMethodTest)", description.getDisplayName());
        assertTrue(description.getChildren().isEmpty());
    }


    @Test
    public void hierarchicalTestMethodStructure() throws Exception {
        System.clearProperty("JUnitParams.flat");

        Description description = testMethod.describe();

        assertEquals("for_others_to_work", description.getDisplayName());
        assertEquals("[0] a (for_others_to_work)(junitparams.internal.TestMethodTest)", description.getChildren().get(0).getDisplayName());
        assertEquals("[1] b (for_others_to_work)(junitparams.internal.TestMethodTest)", description.getChildren().get(1).getDisplayName());
    }
}
