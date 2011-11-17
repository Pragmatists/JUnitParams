package junitparams;

import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.matchers.StringContains;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.reflect.Method;

public class ParametersProvidersTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldPutProviderClassNameInExceptionMessageForProviderWithNoValidMethods() {
        //given
        ParameterisedTestMethodRunner runner = new ParameterisedTestMethodRunner(getTestMethodWithInvalidProvider());

        //when
        exception.expect(RuntimeException.class);
        exception.expectMessage(new StringContains(ProviderClassWithNoValidMethods.class.getName()));
        runner.paramsFromAnnotation();

        //then
        //expected exception
    }

    private TestMethod getTestMethodWithInvalidProvider() {
        Method testMethod = TestClassWithProviderClassWithNoValidMethods.class.getMethods()[0];
        return new TestMethod(new FrameworkMethod(testMethod), new TestClass(TestClassWithProviderClassWithNoValidMethods.class));
    }

    @RunWith(JUnitParamsRunner.class)
    static class TestClassWithProviderClassWithNoValidMethods {
        @Test
        @Parameters(source = ProviderClassWithNoValidMethods.class)
        public void shouldDoNothingItsJustToConnectTestClassWithProvider() {
        }
    }

    static class ProviderClassWithNoValidMethods {
    }
}