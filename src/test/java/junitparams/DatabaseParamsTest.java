package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import junitparams.usage.person_example.PersonSession;
import junitparams.usage.person_example.PersonRowMapper;
import junitparams.usage.person_example.PersonTest.Person;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DatabaseParamsTest {

    @Test
    @DatabaseParameters(sql = "select name, age from persons;",
            url = "jdbc:h2:mem:testdb",
            session = PersonSession.class)
    public void shouldLoadParamsFromDatabaseWithCustomSession(String name, int age) {
        assertThat(name).isNotEmpty();
        assertThat(age).isPositive();
    }

    @Test
    @DatabaseParameters(sql = "select name, age from persons;",
            url = "jdbc:h2:mem:testdb", driver = "org.h2.Driver", user = "sa", password = "sa",
            session = PersonSession.class, mapper = PersonRowMapper.class)
    public void shouldLoadParamsFromDatabaseWithCustomSessionAndMapper(Person person) {
        assertThat(person.getName()).isEqualTo(person.getName().toUpperCase());
        assertThat(person.getAge()).isNegative();
    }
}
