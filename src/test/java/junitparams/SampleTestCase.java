package junitparams;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class SampleTestCase {

    @Test
    public void firstTestMethod() throws Exception {

    }

    @Test
    @Parameters(method = "getParameters")
    public void secondTestMethod(String parameter) throws Exception {

    }

    private Object[] getParameters() {
        return new Object[]{"a", "b"};
    }

}
