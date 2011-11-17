package junitparams;

import java.lang.reflect.*;

import org.junit.*;
import org.junit.internal.matchers.*;
import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;

public class ParametersProvidersTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldPutProviderClassNameInExceptionMessageForProviderWithNoValidMethods() {
        ParameterisedTestMethodRunner runner = new ParameterisedTestMethodRunner(getTestMethodWithInvalidProvider());

        exception.expect(RuntimeException.class);
        exception.expectMessage(new StringContains(ProviderClassWithNoValidMethods.class.getName()));
        runner.paramsFromAnnotation();
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