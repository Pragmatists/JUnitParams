package junitparams;

import static junitparams.internal.Utils.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.*;
import org.junit.runner.*;

import junitparams.internal.*;

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
    @Parameters
    public void shouldCreateParameterObjectsOnce(Object object) {

    }

    public Object[] parametersForShouldCreateParameterObjectsOnce() {
        return new Object[] { new A() };
    }

    class A {
        String test = "test";

        @Override
        public String toString() {
            return test + super.toString();
        }
    }
}
