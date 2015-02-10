package junitparams;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import junitparams.internal.ParametrisedTestMethodRunner;
import junitparams.internal.TestMethod;

@RunWith(JUnitParamsRunner.class)
public class ParametersReaderProvidersTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    @Parameters(source = SingleParamSetProvider.class)
    public void oneParamSetFromClass(String a, String b) {
        assertThat(a).isEqualTo("a");
        assertThat(b).isEqualTo("b");
    }

    public static class SingleParamSetProvider {
        public static Object[] provideOneParamSetSameTypes() {
            return new Object[]{"a", "b"};
        }
    }

    @Test
    public void shouldPutProviderClassNameInExceptionMessageForProviderWithNoValidMethods() {
        TestClass testClass = new TestClass(TestClassWithProviderClassWithNoValidMethods.class);

        ParametrisedTestMethodRunner runner = new ParametrisedTestMethodRunner(getTestMethodWithInvalidProvider(testClass));

        exception.expect(RuntimeException.class);
        exception.expectMessage(ProviderClassWithNoValidMethods.class.getName());
        TestMethod.listFrom(Arrays.<FrameworkMethod>asList(runner.method), testClass);
    }

    private TestMethod getTestMethodWithInvalidProvider(TestClass testClass) {
        Method testMethod = testClass.getJavaClass().getMethods()[0];
        return new TestMethod(new FrameworkMethod(testMethod), testClass, new Object[]{}, 0);
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
        assertThat(integer).isLessThan(4);
    }

    public static class OneIntegerProvider {
        public static Object[] provideTwoNumbers() {
            return new Object[]{new Object[]{1}, new Object[]{2}};
        }

        public static Object[] provideOneNumber() {
            return new Object[] {new Object[]{3}};
        }
    }

    @Test
    @Parameters(source = DomainObjectProvider.class)
    public void providedDomainParams(DomainClass object1, DomainClass object2) {
        assertThat(object1.toString()).isEqualTo("dupa1");
        assertThat(object2.toString()).isEqualTo("dupa2");
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