package junitparams;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("unused")
@RunWith(JUnitParamsRunner.class)
public class ParametersForMethodWithParamsTest {
    private static HashMap<String, Integer> keyValues = new HashMap<String, Integer>();
    static {
    	// Can't be put in a method annotated with @BeforeClass
    	// since this will be used even before @BeforeClass is invoked
    	keyValues.put("one", 1);
    	keyValues.put("two", 2);
    	keyValues.put("three", 3);
    }
    
    @Test
    @Parameters(methodParams={"one"})
    public void methodParamsWithoutMethod(int number) {
        assertThat(number).isGreaterThan(0);
    }
    private Object[] parametersForMethodParamsWithoutMethod(String[] args){
    	Object[] result = new Object[args.length];
    	for(int i=0; i<args.length; i++){
    		result[i] = keyValues.get(args[i]);
    	}
    	return result;
    }
    private Object[] parametersForMethodParamsWithoutMethod(){
    	// Should not be called
    	return null;
    }
    
    @Test
    @Parameters(method="parametersForMethodParamsWithoutMethod", methodParams={"two"})
    public void methodParamsWithMethod(int number){
    	assertThat(number).isGreaterThan(0);
    }
    
    @Test
    @Parameters(method="parametersForMethodParamsWithoutMethod", methodParams={"one", "two"})
    public void methodParamsContainsMultipleElements(int number){
    	assertThat(number).isGreaterThan(0);
    }
    
    @Test
    @Parameters(method="parametersForNoMethodParams", methodParams={})
    public void emptyMethodParamsShouldFindMethodWithoutArgument(int number){
    	assertThat(number).isGreaterThan(0);
    }
    private Object[] parametersForNoMethodParams(){
    	return $(1);
    }
    private Object[] parametersForNoMethodParams(String[] args){
    	// Should not be called
    	return null;
    }
}
