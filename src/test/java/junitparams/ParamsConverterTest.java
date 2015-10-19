package junitparams;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.converters.ConversionFailedException;
import junitparams.converters.ConvertParam;
import junitparams.converters.Converter;
import junitparams.converters.Param;
import junitparams.converters.ParamConverter;

import static org.assertj.core.api.Assertions.*;

@RunWith(JUnitParamsRunner.class)
public class ParamsConverterTest {

    @Test
    @Parameters({"01.12.2012"})
    public void convertSingleParam(
            @ConvertParam(value = StringToDateConverter.class, options = "dd.MM.yyyy") Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    @Test
    @Parameters({"01.12.2012,A"})
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
    @Parameters({"01.12.2012"})
    public void convertParamsUsingParamAnnotation(@Param(converter = SimpleDateConverter.class) Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    @Test
    @Parameters({"01.12.2012"})
    public void convertParamsUsingCustomParamAnnotation(@DateParam Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    @Test
    @Parameters(method = "params")
    public void convertParamsFromMethodUsingCustomParamAnnotation(@DateParam Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    private List<String> params() {
        return Arrays.asList("01.12.2012");
    }

    @Test
    @Parameters({"2012-12-01"})
    public void convertParamsUsingCustomParamAnnotationOverridingAttributes(@DateParam(format = "yyyy-MM-dd") Date date) {
        Calendar calendar = createCalendarWithDate(date);
        assertCalendarDate(calendar);
    }

    @Test
    @Parameters({"2012-12-01"})
    public void passesParametersWithOtherAnnotations(@Other String parameter) {
        assertThat(parameter)
                .isExactlyInstanceOf(String.class)
                .isEqualTo("2012-12-01");
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
    @Target(ElementType.PARAMETER)
    @Param(converter = FormattedDateConverter.class)
    public @interface DateParam {

        String format() default "dd.MM.yyyy";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    public @interface Other {
    }

    public static class FormattedDateConverter implements Converter<DateParam, Date> {

        private String format;

        @Override
        public void initialize(DateParam annotation) {
            this.format = annotation.format();
        }

        @Override
        public Date convert(Object param) throws ConversionFailedException {
            try {
                return new SimpleDateFormat(format).parse(param.toString());
            } catch (ParseException e) {
                throw new ConversionFailedException("failed");
            }
        }
    }

    public static class SimpleDateConverter implements Converter<Param, Date> {
        @Override
        public void initialize(Param annotation) {
        }

        @Override
        public Date convert(Object param) throws ConversionFailedException {
            try {
                return new SimpleDateFormat("dd.MM.yyyy").parse(param.toString());
            } catch (ParseException e) {
                throw new ConversionFailedException("failed");
            }
        }
    }
}
