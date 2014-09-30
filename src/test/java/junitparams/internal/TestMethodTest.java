package junitparams.internal;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

import junitparams.*;
import static junitparams.JUnitParamsRunner.$;

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
        System.clearProperty("JUnitParams.flat");
        Description description = plainTestMethod.describe();

        assertEquals("for_others_to_work", description.getDisplayName());
        assertEquals("[0] a (for_others_to_work)(junitparams.internal.TestMethodTest)", description.getChildren().get(0).getDisplayName());
        assertEquals("[1] b (for_others_to_work)(junitparams.internal.TestMethodTest)", description.getChildren().get(1).getDisplayName());
    }

    @Test
    public void hierarchicalArrayTestMethodStructure() throws Exception {
        System.clearProperty("JUnitParams.flat");
        Description description = arrayTestMethod.describe();

        assertEquals("for_others_to_work_with_array", description.getDisplayName());
        assertEquals("[0] a,b (for_others_to_work_with_array)(junitparams.internal.TestMethodTest)",
                description.getChildren().get(0).getDisplayName());
        assertEquals("[1] b,a (for_others_to_work_with_array)(junitparams.internal.TestMethodTest)",
                description.getChildren().get(1).getDisplayName());
    }
    
    @Test
    @Parameters
    public void testVarargs(String... strs){
    	assertArrayEquals("Hello world".split(" "), strs);
    }
    
    protected Object[] parametersForTestVarargs(){
    	return new Object[]{
    			$("Hello", "world")
    	};
    }
    
    @Test
    @Parameters
    public void testVarargsCustomClass(Pair... pairs){
		assertEquals(pairs[0].x, pairs[0].y);
		assertEquals(pairs[1].x, pairs[1].y);
		assertNotEquals(pairs[2].x, pairs[2].y);
    }
    
    protected Object[] parametersForTestVarargsCustomClass(){
    	return new Object[]{
    			$(new Pair(0,0), new Pair(1,1), new Pair(2,3))
    	};
    }
    
    @Test
    @Parameters
    public void testVarargsMoreArgs(int xVal, Pair... pairs){
		assertEquals(xVal, pairs[0].x);
		assertNotEquals(xVal, pairs[1].x);
    }
    
    protected Object[] parametersForTestVarargsMoreArgs(){
    	return new Object[]{
    			$(10, new Pair(10,20), new Pair(20,30))
    	};
    }
    
    private class Pair{
    	int x,y;
    	public Pair(int x, int y){
    		this.x = x;
    		this.y = y;
    	}
    }
}
