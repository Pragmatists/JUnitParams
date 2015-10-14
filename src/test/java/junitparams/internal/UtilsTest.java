package junitparams.internal;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class UtilsTest {

    @Test
    public void shouldSafelyCastStringArrayParamSetToArray() {
        // given
        Object paramSet = new String[] {"this", "is", "a", "test"};

        // when
        Object[] result = Utils.safelyCastParamsToArray(paramSet);

        // then
        assertThat(result).containsExactly("this", "is", "a", "test");
    }

    @Test
    public void shouldSafelyCastIntegerArrayParamSetToArray() {
        // given
        Object paramSet = new Integer[] {1, 2, 3, 4};

        // when
        Object[] result = Utils.safelyCastParamsToArray(paramSet);

        // then
        assertThat(result).containsExactly(1, 2, 3, 4);
    }

    @Test
    public void shouldSafelyCastArrayParamSetToArray() {
        // given
        Object paramSet = new Object[] {1, "2", 30D};

        // when
        Object[] result = Utils.safelyCastParamsToArray(paramSet);

        // then
        assertThat(result).containsExactly(1, "2", 30D);
    }

    @Test
    public void shouldCreateSingletonArrayWhenCastingObjectToArray() {
        // given
        Object paramSet = "test";

        // when
        Object[] result = Utils.safelyCastParamsToArray(paramSet);

        // then
        assertThat(result).containsExactly("test");
    }
}
