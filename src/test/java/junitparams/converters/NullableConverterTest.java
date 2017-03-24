package junitparams.converters;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    @Parameters({"1, null"})
    public void shouldCastParamsToIntegerType(@Nullable Integer integerParam, @Nullable Integer nullInteger) {
        assertThat(integerParam).isEqualTo(Integer.valueOf(1));
        assertThat(nullInteger).isNull();
    }

    @Test
    @Parameters({"1, null"})
    public void shouldCastParamsToLongType(@Nullable Long longParam, @Nullable Long nullLongParam) {
        assertThat(longParam).isEqualTo(Long.valueOf("1"));
        assertThat(nullLongParam).isNull();
    }

    @Test
    @Parameters({"1.0, null"})
    public void shouldCastParamsToFloatType(@Nullable Float floatParam, @Nullable Float nullFloatParam) {
        assertThat(floatParam).isEqualTo(Float.valueOf("1.0"));
        assertThat(nullFloatParam).isNull();
    }

    @Test
    @Parameters({"1.0, null"})
    public void shouldCastParamsToDoubleType(@Nullable Double doubleParam, @Nullable Double nullDoubleParam) {
        assertThat(doubleParam).isEqualTo(Double.valueOf("1.0"));
        assertThat(nullDoubleParam).isNull();
    }

    @Test
    @Parameters({"1, null"})
    public void shouldCastParamsToShortType(@Nullable Short shortParam, @Nullable Short nullShotParam) {
        assertThat(shortParam).isEqualTo(Short.valueOf("1"));
        assertThat(nullShotParam).isNull();
    }

    @Test
    @Parameters({"true, null"})
    public void shouldCastParamsToBooleanType(@Nullable Boolean booleanParam, @Nullable Boolean nullBooleanParam) {
        assertThat(booleanParam).isEqualTo(Boolean.TRUE);
        assertThat(nullBooleanParam).isNull();
    }

    @Test
    @Parameters({"A, null"})
    public void shouldCastParamsToCharacterType(@Nullable Character charParam, @Nullable Character nullCharacterParam) {
        assertThat(charParam).isEqualTo(Character.valueOf('A'));
        assertThat(nullCharacterParam).isNull();
    }

    @Test
    @Parameters({"1, null"})
    public void shouldCastParamsToCharacterType(@Nullable BigDecimal bigDecimalParam, @Nullable Character nullBigDecimalParam) {
        assertThat(bigDecimalParam).isEqualTo(BigDecimal.ONE);
        assertThat(nullBigDecimalParam).isNull();
    }

    @Test
    @Parameters({"VALUE, null"})
    public void shouldCastParamsToEnumType(@Nullable Type enumParam, @Nullable Type nullEnumParam) {
        assertThat(enumParam).isEqualTo(Type.VALUE);
        assertThat(nullEnumParam).isNull();
    }

    private enum Type {
        VALUE
    }

    @Test
    @Parameters({"1, #null# "})
    public void shouldUseCustomNullIdentifierForDifferentType(@Nullable(nullIdentifier = "#null#") Integer value,
                                                              @Nullable(nullIdentifier = "#null#") Integer shouldBeNull) {
        assertThat(value).isEqualTo(1);
        assertThat(shouldBeNull).isNull();
    }

    @Test
    @Parameters({"#null# , $null$ "})
    public void shouldUseDifferentCustomNullIdentifiers(@Nullable(nullIdentifier = "#null#") String first,
                                                        @Nullable(nullIdentifier = "$null$") String second) {
        assertThat(first).isNull();
        assertThat(second).isNull();
    }

}
