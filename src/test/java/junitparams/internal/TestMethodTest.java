package junitparams.internal;

import static junitparams.JUnitParamsRunner.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

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
        assertThat(arg).isIn("a","b");
    }


    @Test
    @Parameters({"a,b","b,a"})
    public void for_others_to_work_with_array(String... arg) throws Exception {
        assertThat(arg).containsOnlyOnce("a","b");
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
    public void testVarargsMoreArgs(int sumOfX, int sumOfY, Pair... pairs){
        int sumOfXFromPairs = 0;
        int sumOfYFromPairs = 0;
        for (Pair pair : pairs) {
            sumOfXFromPairs += pair.x;
            sumOfYFromPairs += pair.y;
        }
        assertEquals(sumOfX, sumOfXFromPairs);
        assertEquals(sumOfY, sumOfYFromPairs);
    }
    
    protected Object parametersForTestVarargsMoreArgs(){
    	return $(
                    $(40, 50, new Pair(17,21), new Pair(12,18), new Pair(11,11)),
                    $(10, 20, new Pair(3,15), new Pair(7,5))
                );
    }

    @Test
    @Parameters
    public void testVargsMoreArgsTheSameType(Pair sum, Pair... pairs) {
        assertEquals(sum.x, pairs[0].x + pairs[1].x);
        assertEquals(sum.y, pairs[0].y + pairs[1].y);
    }

    protected Object parametersForTestVargsMoreArgsTheSameType(){
        return $(
                    $(new Pair(10,30), new Pair(7,17), new Pair(3,13)),
                    $(new Pair(20,40), new Pair(18,21), new Pair(2,19))
                );
    }

    
    private class Pair{
    	int x,y;
    	public Pair(int x, int y){
    		this.x = x;
    		this.y = y;
    	}
    }
}
