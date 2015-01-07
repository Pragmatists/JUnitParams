package junitparams.usage;

import static junitparams.JUnitParamsRunner.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

import junitparams.*;
import junitparams.converters.*;
import junitparams.mappers.*;
import junitparams.usage.person_example.*;

@RunWith(JUnitParamsRunner.class)
public class Samples_of_Usage_Test {

    @Test
    @Parameters({"AAA,1", "BBB,2"})
    public void params_in_annotation(String p1, Integer p2) { }

    @Test
    @Parameters
    public void params_in_default_method(String p1, Integer p2) { }
    private Object parametersForParams_in_default_method() { return $($("AAA", 1), $("BBB", 2)); }

    @Test
    @Parameters(method = "named1")
    public void params_in_named_method(String p1, Integer p2) { }
    private Object named1() { return $($("AAA", 1)); }

    @Test
    @Parameters(method = "named2,named3")
    public void params_in_multiple_methods(String p1, Integer p2) { }
    private Object named2() { return $($("AAA", 1)); }
    private Object named3() { return $($("BBB", 2)); }

    @Test
    @Parameters(source = ParametersReaderProvidersTest.OneIntegerProvider.class)
    public void parameters_from_external_class(int integer) { }

    @Test
    @Parameters
    public void params_in_collection(String p1) { }
    private List<String> parametersForParams_in_collection() { return Arrays.asList("a"); }

    @Test
    @Parameters
    public void params_in_iterator(String p1) { }
    private Iterator<String> parametersForParams_in_iterator() { return Arrays.asList("a").iterator(); }

    @Test
    @Parameters({"SOME_VALUE", "OTHER_VALUE"})
    public void enums_as_params_in_annotation(PersonType person) { }

    @Test
    @Parameters
    public void enums_as_params_in_method(PersonType person) { }
    private PersonType[] parametersForEnums_as_params_in_method() { return (PersonType[]) new PersonType[] {PersonType.SOME_VALUE}; }

    @Test
    @Parameters(source = PersonType.class)
    public void enum_as_source(PersonType personType) {
    }

    @Test
    @Parameters
    public void wrap_params_with_constructor(PersonTest.Person person) { }
    private Object parametersForWrap_params_with_constructor() { return $($("first", 1), $("second", 2)); }

    @Test
    @FileParameters("src/test/resources/test.csv")
    public void load_params_from_csv(int age, String name) { }

    @Test
    @FileParameters(value = "src/test/resources/test.csv", mapper = PersonMapper.class)
    public void load_params_from_any_file(PersonTest.Person person) { }

    @Test
    @FileParameters("classpath:test.csv")
    public void load_params_from_classpath(int age, String name) { }

    @Test
    @FileParameters(value = "classpath:with_header.csv", mapper = CsvWithHeaderMapper.class)
    public void load_params_from_csv_with_header(int id, String name) { }

    @Test
    @DatabaseParameters(sql = "select current_date();", url = "jdbc:h2:mem:testdb")
    public void load_params_from_database(Date date) { }

    @Test
    @DatabaseParameters(sql = "select name, age from persons;", url = "jdbc:h2:mem:testdb", session = PersonSession.class)
    public void load_params_from_database_with_custom_session(String name, int age) { }

    @Test
    @DatabaseParameters(sql = "select name, age from persons;",
            url = "jdbc:h2:mem:testdb", driver = "org.h2.Driver", user = "sa", password = "sa",
            session = PersonSession.class, mapper = PersonRowMapper.class)
    public void load_params_from_database_with_custom_session_and_mapper(PersonTest.Person person) { }

    @Test
    @Parameters({ "01.12.2012" })
    public void convert_params(@ConvertParam(value = ParamsConverterTest.StringToDateConverter.class, options = "dd.MM.yyyy") Date date) {}

}
