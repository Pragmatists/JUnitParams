package junitparams.converters;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static org.assertj.core.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
public class NullableConverterTest {

    @Test
    @Parameters({"null"})
    public void shouldConvertToNull(@Nullable String value) {
        assertThat(value).isNull();
    }

    @Test
    @Parameters({" null"})
    public void shouldConvertToNullIgnoringWhitespaces(@Nullable String value) {
        assertThat(value).isNull();
    }

    @Test
    @Parameters({"A", "B"})
    public void shouldNotApplyConversionToNull(@Nullable String value) {
        assertThat(value).isNotNull();
    }

    @Test
    @Parameters({" #null "})
    public void shouldUseCustomNullIdentifier(@Nullable(nullIdentifier = "#null") String value) {
        assertThat(value).isNull();
    }

    @Test
    @Parameters({" null "})
    public void shouldIgnoreDefaultNulllIdentifierWhenIsSpecifiedCustomOne(@Nullable(nullIdentifier = "#null") String value) {
        assertThat(value).isNotNull();
    }

    @Test
    @Parameters({"A, B"})
    public void shouldNotApplyConversionToNull(@Nullable String firstParam, @Nullable String secondParam) {
        assertThat(firstParam).isEqualTo("A");
        assertThat(secondParam).isEqualTo("B");
    }


}
