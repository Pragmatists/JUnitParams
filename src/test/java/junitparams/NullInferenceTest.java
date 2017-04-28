package junitparams;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class NullInferenceTest {

    @Test
    @Parameters({
            "/null/,, ",
            "/null/, /two/, three"
    })
    public void shouldInferNullAsFirstParameter(String first, String second, String third) {
        assertThat(first).isNull();
        assertThat(second).isNotNull();
        assertThat(third).isNotNull();
    }

    @Test
    @Parameters({
            ", /null/,",
            "one, /null/, three"
    })
    public void shouldInferNullAsMiddleParameter(String first, String second, String third) {
        assertThat(first).isNotNull();
        assertThat(second).isNull();
        assertThat(third).isNotNull();
    }

    @Test
    @Parameters({
            ",,/null/",
            "one, two, /null/"
    })
    public void shouldInferNullAsLastParameter(String first, String second, String third) {
        assertThat(first).isNotNull();
        assertThat(second).isNotNull();
        assertThat(third).isNull();
    }

    @Test
    @Parameters({
            "/null", "null/", "null", "/somedata/"
    })
    public void shouldNotInferNullValue(String inferred) {
        assertThat(inferred).isNotNull();
    }

    @Test
    @Parameters({
            "/NULL/", "/null/", "/Null/", "/nuLL/"
    })
    public void shoudInferNullRagardlessToCase(String inferred) {
        assertThat(inferred).isNull();
    }

}
