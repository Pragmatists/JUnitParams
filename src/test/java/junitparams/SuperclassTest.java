package junitparams;

import static junitparams.JUnitParamsRunner.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public abstract class SuperclassTest {
	
	@Test
	@Parameters(method = "paramForSuperclassMethod")
	public void testWorky(int val) throws Exception {
		assertTrue(val > 0);
	}	

    protected Object[] paramsForIsAdult() {
        return $($(11, false),
                $(17, false),
                $(18, true),
                $(22, true));
    }
}