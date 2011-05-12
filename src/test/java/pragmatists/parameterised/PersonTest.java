package pragmatists.parameterised;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class PersonTest {

    @Test
    @Parameters({ "17, false", "22, true" })
    public void greaterAdultAge(int number, boolean valid) throws Exception {
        assertEquals(valid, number > 18);
    }

    @Test
    @Parameters(source = PersonProvider.class)
    public void personIsAdult(Person person, boolean valid) {
        assertEquals(valid, person.getAge() > 18);
    }

    public static class PersonProvider {
        public static Object[] provideAdults() {
            return params(
                    params(new Person(25), true),
                    params(new Person(32), true));
        }

        public static Object[] provideTeens() {
            return params(
                    params(new Person(12), false),
                    params(new Person(17), false));
        }

        private static Object[] params(Object... param) {
            return param;
        }
    }

    public static class Person {
        private int age;

        public Person(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person of age: " + age;
        }
    }
}