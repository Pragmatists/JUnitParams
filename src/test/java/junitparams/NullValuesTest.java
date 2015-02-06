package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class NullValuesTest {

    private Object[] expectedSingleParams = new Object[]{null, "string", Long.valueOf(1)};

    private static int expectedSingleParamsIndex;

    private Object[] expectedMultipleParams = multipleParams();
    private static int expectedMultipleParamsIndex;

    @Test
    @Parameters(method = "singleParams")
    public void passesForSingleParametersListWithTheWorkaround(Object param) {
        assertThat(param).isEqualTo(expectedSingleParams[expectedSingleParamsIndex]);
        expectedSingleParamsIndex++;
    }

    public Object[] singleParams() {
        return new Object[]{new Object[]{null}, "string", Long.valueOf(1)};
    }

    @Test
    @Parameters(method = "multipleParams")
    public void passesForMultipleParametersOutOfBox(Object param1, Object param2, Object param3) {
        assertThat(param1).isEqualTo(((Object[]) expectedMultipleParams[expectedMultipleParamsIndex])[0]);
        assertThat(param2).isEqualTo(((Object[]) expectedMultipleParams[expectedMultipleParamsIndex])[1]);
        assertThat(param3).isEqualTo(((Object[]) expectedMultipleParams[expectedMultipleParamsIndex])[2]);
        expectedMultipleParamsIndex++;
    }

    public Object[] multipleParams() {
        return new Object[]{new Object[]{null, "string", null}, new Object[]{"string", Long
                .valueOf(1), null}, new Object[]{null, null, null}};
    }

}
