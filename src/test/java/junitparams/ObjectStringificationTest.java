package junitparams;

import junitparams.internal.Utils;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junitparams.JUnitParamsRunner.$;
import static junitparams.internal.Utils.stringify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


@RunWith(JUnitParamsRunner.class)
public class ObjectStringificationTest {

    @Test
    public void stringifyString() throws Exception {
        String obj = "test";

        assertThat(stringify(obj, 0)).isEqualTo("[0] test");
    }

    @Test
    public void stringifyClassWithToStringAndOneParam() throws Exception {
        ClassWithToString obj = new ClassWithToString("test");

        assertThat(stringify(obj, 0)).isEqualTo("[0] test");
    }

    @Test
    public void stringifyClassWithToStringAndManyParams() throws Exception {
        ClassWithToString obj1 = new ClassWithToString("one");
        ClassWithToString obj2 = new ClassWithToString("two");

        assertThat(stringify(new Object[] { obj1, obj2 }, 0)).isEqualTo("[0] one, two");
    }

    @Test
    public void stringifyClassWithToStringInSuperclass() throws Exception {
        ClassWithToString obj = new ClassWithToString("dupa") {
        };

        assertThat(Utils.stringify(obj, 0)).isEqualTo("[0] dupa");
    }

    private class ClassWithToString {
        private String description;

        public ClassWithToString(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    @Test
    public void stringifyArray() {
        Double[] nullArray = null;
        int[] primitiveArray = {1, 2, 3};
        String[] array = {"one", "two", null};
        Object[] mixed = $(
                $(nullArray, "stringOne", primitiveArray, "stringTwo", array)
        );

        assertThat(Utils.stringify(mixed)).isEqualTo("null, stringOne, [1, 2, 3], stringTwo, [one, two, null]");
    }

    @Test
    @Parameters
    public void shouldCreateParameterObjectsOnce(Object object) {
        assertThat(object).isInstanceOf(A.class);
    }

    public Object[] parametersForShouldCreateParameterObjectsOnce() {
        return new Object[] { new A() };
    }

    static class A {
        static int instances = 0;
        String test = "test";

        A() {
            if (++instances > 1) {
                fail();
            }
        }

        @Override
        public String toString() {
            return test + super.toString();
        }
    }
}
