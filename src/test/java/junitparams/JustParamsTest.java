package junitparams;

import static junitparams.JUnitParamsRunner.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class JustParamsTest {

    @Test
    @Parameters({ "1", "2" })
    public void singleParam(int number) {
        assertTrue(number > 0);
    }

    @Test
    @Parameters
    public void oneParamDifferentTypes(int number, String a) {
        assertTrue(number > 0);
    }

    private Object[] parametersForOneParamDifferentTypes() {
        return $($(1, "a"));
    }

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
    @Parameters
    public void oneParamSetOneNull(String a, String b) {
    }

    private Object[] parametersForOneParamSetOneNull() {
        return $($(null, "b"));
    }

    @Test
    @Parameters
    public void noToString(NoToStringObject o) {
    }

    private Object[] parametersForNoToString() {
        return $($(new NoToStringObject()));
    }

    public class NoToStringObject {
    }

    @Test
    @Parameters({ "a \n b", "a(asdf)" })
    public void specialCharsInParam(String a) throws Exception {
    }

    @Test
    @Parameters({ "1, false", "2, true" })
    public void multipleParams(int number, boolean what) throws Exception {
        assertEquals(what, number > 1);
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

    @Test
    @Parameters
    public void emptyParamset() {
    }

    private Object[] parametersForEmptyParamset() {
        return new Object[] {};
    }

}
