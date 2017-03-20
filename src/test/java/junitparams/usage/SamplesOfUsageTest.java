package junitparams.usage;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.ParametersReaderProvidersTest;
import junitparams.ParamsConverterTest;
import junitparams.converters.Nullable;
import junitparams.converters.Param;
import junitparams.custom.combined.CombinedParameters;
import junitparams.mappers.CsvWithHeaderMapper;
import junitparams.naming.TestCaseName;
import junitparams.usage.person_example.PersonMapper;
import junitparams.usage.person_example.PersonTest;
import junitparams.usage.person_example.PersonType;

import static junitparams.JUnitParamsRunner.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertNull;

@RunWith(JUnitParamsRunner.class)
public class SamplesOfUsageTest {

    @Test
    @Parameters({"AAA,1", "BBB,2"})
    public void paramsInAnnotation(String p1, Integer p2) { }

    @Test
    @Parameters({"AAA|1", "BBB|2"})
    public void paramsInAnnotationPipeSeparated(String p1, Integer p2) { }
    
    @Test
    @Parameters({"null", "ANY_VALUE"})
    public void nullableParams(@Nullable String p1) {}
    
    @Test
    @Parameters({"25", "null"})
    public void nullableNumericParams(@Nullable Integer p1) {}
    
    @Test
    @Parameters({"#null"})
    public void customNullIdentifier(@Nullable("#null") Integer p1) {
    	assertThat(p1).isNull();
    }

    @Test
    @Parameters
    public void paramsInDefaultMethod(String p1, Integer p2) { }
    private Object parametersForParamsInDefaultMethod() {
        return new Object[]{new Object[]{"AAA", 1}, new Object[]{"BBB", 2}};
    }

    @Test
    @Parameters(method = "named1")
    public void paramsInNamedMethod(String p1, Integer p2) { }
    private Object named1() {
        return new Object[]{"AAA", 1};
    }

    @Test
    @Parameters(method = "named2,named3")
    public void paramsInMultipleMethods(String p1, Integer p2) { }
    private Object named2() {
        return new Object[]{"AAA", 1};
    }
    private Object named3() {
        return new Object[]{"BBB", 2};
    }

    @Test
    @Parameters(method = "named4")
    public void paramsWithVarargs(String... args) {
        assertThat(args).isEqualTo(new String[]{"AAA", "BBB"});
    }
    private Object named4() { return new Object[]{new String[]{"AAA", "BBB"}}; }

    @Test
    @Parameters(source = ParametersReaderProvidersTest.OneIntegerProvider.class)
    public void paramsFromExternalClass(int integer) { }

    @Test
    @Parameters
    public void paramsInCollection(String p1) { }
    private List<String> parametersForParamsInCollection() { return Arrays.asList("a"); }

    @Test
    @Parameters
    public void paramsInIterator(String p1) { }
    private Iterator<String> parametersForParamsInIterator() { return Arrays.asList("a").iterator(); }

    @Test
    @Parameters({"SOME_VALUE", "OTHER_VALUE"})
    public void enumsAsParamInAnnotation(PersonType person) { }

    @Test
    @Parameters
    public void enumsAsParamsInMethod(PersonType person) { }
    private PersonType[] parametersForEnumsAsParamsInMethod() { return (PersonType[]) new PersonType[] {PersonType.SOME_VALUE}; }

    @Test
    @Parameters(source = PersonType.class)
    public void enumAsSource(PersonType personType) {
    }

    @Test
    @Parameters
    public void wrapParamsWithConstructor(PersonTest.Person person) { }
    private Object parametersForWrapParamsWithConstructor() {
        return new Object[]{new Object[]{"first", 1}, new Object[]{"second", 2}};
    }

    @Test
    @FileParameters("src/test/resources/test.csv")
    public void loadParamsFromCsv(int age, String name) { }

    @Test
    @FileParameters(value = "src/test/resources/test.csv", mapper = PersonMapper.class)
    public void loadParamsFromAnyFile(PersonTest.Person person) { }

    @Test
    @FileParameters("classpath:test.csv")
    public void loadParamsFromClasspath(int age, String name) { }

    @Test
    @FileParameters(value = "classpath:with_header.csv", mapper = CsvWithHeaderMapper.class)
    public void loadParamsFromCsvWithHeader(int id, String name) { }

    @Test
    @Parameters({ "01.12.2012" })
    public void convertParams(@Param(converter = ParamsConverterTest.SimpleDateConverter.class) Date date) {}

    @Test
    @Parameters("please\\, escape commas if you use it here and don't want your parameters to be splitted")
    public void commasInParametersUsage(String phrase) { }

    @Test
    @Parameters({ "1,1", "2,2", "3,6" })
    @TestCaseName("factorial({0}) = {1}")
    public void customNamesForTestCase(int argument, int result) { }

    @Test
    @Parameters({ "value1, value2", "value3, value4" })
    @TestCaseName("[{index}] {method}: {params}")
    public void predefinedMacroForTestCaseNames(String param1, String param2) { }

    public Object mixedParameters() {
        boolean booleanValue = true;
        int[] primitiveArray = {1, 2, 3};
        String stringValue = "Test";
        String[] stringArray = {"one", "two", null};
        return $(
                $(booleanValue, primitiveArray, stringValue, stringArray)
        );
    }

    @Test
    @Parameters(method = "mixedParameters")
    @TestCaseName("{0}, {1}, {2}, {3}")
    public void usageOfMultipleTypesOfParameters(
            boolean booleanValue, int[] primitiveArray, String stringValue, String[] stringArray) {
    }

    @Test
    @CombinedParameters({"AAA,BBB", "1|2"})
    public void combineParamsInAnnotation(String p1, Integer p2) {}

}
