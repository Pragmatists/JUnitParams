package junitparams.internal;

import static org.junit.Assert.assertEquals;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

import junitparams.*;

@RunWith(JUnitParamsRunner.class)
public class TestMethodTest {
    TestMethod testMethod;

    @Before
    public void setUp() throws Exception {
        testMethod = new TestMethod(new FrameworkMethod(TestMethodTest.class.getMethod("sampleTest")),
                new TestClass(this.getClass()));
    }

    @Test
    @Ignore
    @Parameters({"a","b"})
    public void sampleTest(String arg) throws Exception {
    }

    @Test
    @Ignore
    public void flatTestMethodStructure() throws Exception {
        Description expectedDescription = Description.createTestDescription(this.getClass(), "sampleTest");

        Description description = testMethod.describe();

        assertEquals(expectedDescription, description);
    }


    @Test
    @Ignore
    public void hierarchicalTestMethodStructure() throws Exception {
        Description expectedDescription = Description.createTestDescription(this.getClass(), "sampleTest");

        Description description = testMethod.describe();

        assertEquals(expectedDescription, description);
    }
}
