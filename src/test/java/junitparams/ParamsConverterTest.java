package junitparams;

import static org.assertj.core.api.Assertions.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import junitparams.converters.*;

@RunWith(JUnitParamsRunner.class)
public class ParamsConverterTest {

    @Test
    @Parameters({ "01.12.2012" })
    public void convertSingleParam(
        @ConvertParam(value = StringToDateConverter.class, options = "dd.MM.yyyy") Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    @Test
    @Parameters({ "01.12.2012,A" })
    public void convertMultipleParams(
        @ConvertParam(value = StringToDateConverter.class, options = "dd.MM.yyyy") Date date,
        @ConvertParam(LetterToNumberConverter.class) int num) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
        assertThat(num).isEqualTo(1);
    }

    @Test
    @Parameters(method = "params")
    public void convertParamsFromMethod(
            @ConvertParam(value = StringToDateConverter.class, options = "dd.MM.yyyy") Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    @Test
    @Parameters({ "01.12.2012" })
    public void convertParamsUsingStereotype(@DateParam Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    @Test
    @Parameters(method = "params")
    public void convertParamsFromMethodUsingStereotype(@DateParam Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    @Test
    @Parameters({ "2012-12-01" })
    public void convertParamsUsingStereotypeWithOptionOverride(@DateParam(options = "yyyy-MM-dd") Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    @Test
    @Parameters({ "2012-12-01" })
    public void convertParamsUsingMultiLevelStereotype(@IsoDateParam Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    private List<String> params() {
        return Arrays.asList("01.12.2012");
    }

    private void assertCalendarDate(Calendar calendar) {
        assertThat(calendar.get(Calendar.YEAR)).isEqualTo(2012);
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(11);
        assertThat(calendar.get(Calendar.DAY_OF_MONTH)).isEqualTo(1);
    }

    private Calendar createCalendarWithDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static class LetterToNumberConverter implements ParamConverter<Integer> {
        public Integer convert(Object param, String options) {
            return param.toString().charAt(0) - 64;
        }
    }

    public static class StringToDateConverter implements ParamConverter<Date> {
        public Date convert(Object param, String options) {
            try {
                return new SimpleDateFormat(options).parse(param.toString());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @ConvertParam(value = StringToDateConverter.class)
    public @interface DateParam {
        String options() default "dd.MM.yyyy";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @ParamsConverterTest.DateParam(options = "yyyy-MM-dd")
    public @interface IsoDateParam {
    }

}
