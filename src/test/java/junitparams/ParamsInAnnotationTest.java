package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.converters.Nullable;

@RunWith(JUnitParamsRunner.class)
public class ParamsInAnnotationTest {

    @Test
    @Parameters({ "1", "2" })
    public void singleParam(int number) {
        assertThat(number).isGreaterThan(0);
    }

    @Test
    @Parameters({ "1, true", "2, false" })
    public void multipleParamsCommaSeparated(int number, boolean isOne) throws Exception {
        if (isOne)
            assertThat(number).isEqualTo(1);
        else
            assertThat(number).isNotEqualTo(1);
    }

    @Test
    @Parameters({ "1 | true", "2 | false" })
    public void multipleParamsPipeSeparated(int number, boolean isOne) throws Exception {
        if (isOne)
            assertThat(number).isEqualTo(1);
        else
            assertThat(number).isNotEqualTo(1);
    }

    @Test
    @Parameters({ "a \n b", "a(asdf)", "a \r a" })
    public void specialCharsInParam(String a) throws Exception {
        assertThat(a).isIn("a \n b", "a(asdf)", "a \r a");
    }

    @Test
    @Parameters({ ",1"})
    public void emptyFirstParam(String empty, int number) {
        assertThat(empty).isEmpty();
        assertThat(number).isEqualTo(1);
    }

    @Test
    @Parameters({ "1,"})
    public void emptyLastParam(int number, String empty) {
        assertThat(empty).isEmpty();
        assertThat(number).isEqualTo(1);
    }

    @Test
    @Parameters({ "1,,1"})
    public void emptyMiddleParam(int number1, String empty, int number2) {
        assertThat(empty).isEmpty();
        assertThat(number1).isEqualTo(1);
        assertThat(number2).isEqualTo(1);
    }

    @Test
    @Parameters({","})
    public void allParamsEmpty(String empty1, String empty2) {
        assertThat(empty1).isEmpty();
        assertThat(empty2).isEmpty();
    }
    
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