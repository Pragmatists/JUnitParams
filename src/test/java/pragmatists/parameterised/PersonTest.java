package pragmatists.parameterised;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class PersonTest {

    @Test
    @Parameters({
            "17, false",
            "22, true" })
    public void isAdultAge(int age, boolean valid) throws Exception {
        assertEquals(valid, age > 18);
    }

    @Test
    @Parameters(source = PersonProvider.class)
    public void personIsAdult(Person person, boolean valid) {
        assertEquals(valid, person.getAge() > 18);
    }

    public static class PersonProvider {
        public static Object[] provideAdults() {
            return $(
                    $(new Person(25), true),
                    $(new Person(32), true));
        }

        public static Object[] provideTeens() {
            return $(
                    $(new Person(12), false),
                    $(new Person(17), false));
        }

        private static Object[] $(Object... params) {
            return params;
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