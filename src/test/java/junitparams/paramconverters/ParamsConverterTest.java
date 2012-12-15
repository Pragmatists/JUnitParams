package junitparams.paramconverters;

import static org.junit.Assert.*;

import java.util.*;

import junitparams.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class ParamsConverterTest {

    @Test
    @Parameters({ "01.12.2012" })
    public void convertSingleParam(
        @ConvertParam(value = StringToDateConverter.class, options = "dd.MM.yyyy") Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        assertEquals(2012, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Test
    @Parameters({ "01.12.2012,A" })
    public void convertMultipleParams(
        @ConvertParam(value = StringToDateConverter.class, options = "dd.MM.yyyy") Date date,
        @ConvertParam(LetterToNumberConverter.class) int num) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        assertEquals(2012, calendar.get(Calendar.YEAR));
        assertEquals(11, calendar.get(Calendar.MONTH));
        assertEquals(1, calendar.get(Calendar.DAY_OF_MONTH));

        assertEquals(1, num);
    }
}
