package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public abstract class SuperclassTest {
	
	@Test
	@Parameters(method = "paramForSuperclassMethod")
	public void testWorky(int val) throws Exception {
		assertThat(val).isGreaterThan(0);
	}	

    protected Object[] paramsForIsAdult() {
        return new Object[]{new Object[]{11, false}, new Object[]{17, false}, new Object[]{18, true}, new Object[]{22, true}};
    }
}