package junitparams.usage;

import static junitparams.JUnitParamsRunner.$;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.ParametersProvidersTest;
import junitparams.ParamsConverterTest;
import junitparams.converters.ConvertParam;
import junitparams.mappers.CsvWithHeaderMapper;
import junitparams.usage.person_example.PersonMapper;
import junitparams.usage.person_example.PersonTest;
import junitparams.usage.person_example.PersonType;

import org.junit.Test;
import org.junit.runner.RunWith;

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
    @Parameters(source = ParametersProvidersTest.OneIntegerProvider.class)
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
    @Parameters({ "01.12.2012" })
    public void convert_params(@ConvertParam(value = ParamsConverterTest.StringToDateConverter.class, options = "dd.MM.yyyy") Date date) {}
    
    private static HashMap<String, Integer> keyValues = new HashMap<String, Integer>();
    static{
    	keyValues.put("one", 1);
    	keyValues.put("two", 2);
    	keyValues.put("three", 3);
    }
    
    @Test
    @Parameters(methodParams={"one"})
    public void params_in_method_with_params(int n){}
    private Object[] parametersForParams_in_method_with_params(String[] args){
    	Object[] result = new Object[args.length];
    	for(int i=0; i<args.length; i++){
    		result[i] = keyValues.get(args[i]);
    	}
    	return result;
    }
    
    @Test
    @Parameters(method="parametersForParams_in_method_with_params", methodParams={"two", "three"})
    public void params_in_named_method_with_params(int n){}

}
