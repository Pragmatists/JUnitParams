package junitparams;

import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import junitparams.internal.InvokeParameterisedMethod;
import junitparams.internal.TestMethod;

public class NoParamsParametrizedTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void failsWhenEmptyParamset() {
        TestClass testClass = new TestClass(NoParams.class);
        Object[] params = {};
        int index = 0;
        TestMethod testMethod = new TestMethod(new FrameworkMethod(NoParams.class.getMethods()[0]), testClass, params, index);

        exception.expect(CoreMatchers.<Throwable>instanceOf(IllegalStateException.class));
        new InvokeParameterisedMethod(testMethod, testClass, params, index);
    }

    @SuppressWarnings("UnusedDeclaration")
    public static class NoParams {
        public void shouldFailWhenEmptyParamset() {
        }

        public Object[] parametersForShouldFailWhenEmptyParamset() {
            return new Object[]{};
        }
    }

}
