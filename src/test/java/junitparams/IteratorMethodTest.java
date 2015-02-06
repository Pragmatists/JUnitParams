package junitparams;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class IteratorMethodTest {
	private static boolean firstCalled = false;
	
    @Test
    @Parameters
    public void shouldHandleIteratorsWithObject(String a) {
    	assertEquals(1, a.length());
    	if(a.equals("a")) firstCalled = true;
    	else assertEquals(firstCalled, true);
    }

    public Iterator<Object> parametersForShouldHandleIteratorsWithObject() {
        ArrayList<Object> params = new ArrayList<Object>();
        params.add("a");
        params.add("b");
        return params.iterator();
    }
	
    @Test
    @Parameters
    public void shouldHandleIteratorsWithObjectArray(String a) {
    	assertEquals(1, a.length());
    	if(a.equals("a")) firstCalled = true;
    	else assertEquals(firstCalled, true);
    }

    public Iterator<Object[]> parametersForShouldHandleIteratorsWithObjectArray() {
        ArrayList<Object[]> params = new ArrayList<Object[]>();
        params.add(new Object[]{"a"});
        params.add(new Object[]{"b"});
        return params.iterator();
    }
    
}