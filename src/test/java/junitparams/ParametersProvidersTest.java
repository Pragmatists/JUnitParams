package junitparams;

import static junitparams.JUnitParamsRunner.*;
import static org.junit.Assert.*;

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
    @Parameters(source = SingleParamSetProvider.class)
    public void oneParamSetFromClass(String a, String b) {
    }

    static class SingleParamSetProvider {
        public static Object[] provideOneParamSetSameTypes() {
            return $($("a", "b"));
        }
    }

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

    @Test
    @Parameters(source = OneIntegerProvider.class)
    public void providedPrimitiveParams(int integer) {
        assertTrue(integer < 4);
    }

    public static class OneIntegerProvider {
        public static Object[] provideTwoNumbers() {
            return $($(1), $(2));
        }

        public static Object[] provideOneNumber() {
            return new Object[] { $(3) };
        }
    }

    @Test
    @Parameters(source = DomainObjectProvider.class)
    public void providedDomainParams(DomainClass object1, DomainClass object2) {
        assertEquals("dupa1", object1.toString());
        assertEquals("dupa2", object2.toString());
    }

    public static class DomainObjectProvider {
        public static Object[] provideDomainObject() {
            return new Object[] { new Object[] {
                    new DomainClass("dupa1"),
                    new DomainClass("dupa2") } };
        }
    }

    public static class DomainClass {
        private final String name;

        public DomainClass(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}