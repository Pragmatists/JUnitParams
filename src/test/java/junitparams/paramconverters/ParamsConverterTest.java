package junitparams.paramconverters;

import static org.junit.Assert.*;

import java.text.*;
import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import junitparams.*;
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
        assertEquals(1, num);
    }

    @Test
    @Parameters(method = "params")
    public void convertParamsFromMethod(
            @ConvertParam(value = StringToDateConverter.class, options = "dd.MM.yyyy") Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    private List<String> params() {
        return Arrays.asList("01.12.2012");
    }

    private void assertCalendarDate(Calendar calendar) {
        assertEquals(2012, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
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

}
