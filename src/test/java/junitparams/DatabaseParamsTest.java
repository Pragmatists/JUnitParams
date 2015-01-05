package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import junitparams.usage.person_example.PersonQueryExecutor;
import junitparams.usage.person_example.PersonRowMapper;
import junitparams.usage.person_example.PersonTest.Person;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DatabaseParamsTest {

    @Test
    @DatabaseParameters(sql = "select name, age from persons;",
            url = "jdbc:h2:mem:testdb",
            executor = PersonQueryExecutor.class)
    public void shouldLoadParamsFromDatabaseWithCustomExecutor(String name, int age) {
        assertThat(name).isNotEmpty();
        assertThat(age).isPositive();
    }

    @Test
    @DatabaseParameters(sql = "select name, age from persons;",
            url = "jdbc:h2:mem:testdb", driver = "org.h2.Driver", user = "sa", password = "sa",
            executor = PersonQueryExecutor.class, mapper = PersonRowMapper.class)
    public void shouldLoadParamsFromDatabaseWithCustomExecutorAndMapper(Person person) {
        assertThat(person.getName()).isEqualTo(person.getName().toUpperCase());
        assertThat(person.getAge()).isNegative();
    }
}
