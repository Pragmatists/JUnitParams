package junitparams;

import static org.assertj.core.api.Assertions.assertThat;

import junitparams.usage.person_example.PersonQueryExecutor;
import junitparams.usage.person_example.PersonResultSetMapper;
import junitparams.usage.person_example.PersonTest.Person;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class DatabaseParamsTest {

    @Test
    @DatabaseParameters(url = "jdbc:h2:mem:testdb",
            sql = "select name, age from persons;",
            executor = PersonQueryExecutor.class)
    public void shouldLoadParamsWithAnnotationConfigAndSimpleResultSetMapper(String name, int age) {
        assertThat(name).isNotEmpty();
        assertThat(age).isGreaterThan(0);
    }

    @Test
    @DatabaseParameters(url = "jdbc:h2:mem:testdb",
            sql = "select name, age from persons;",
            executor = PersonQueryExecutor.class,
            mapper = PersonResultSetMapper.class)
    public void shouldLoadParamsWithAnnotationConfigAndCustomResultSetMapper(Person person) {
        assertThat(person.getName()).isEqualTo(person.getName().toUpperCase());
        assertThat(person.getAge()).isGreaterThan(0);
    }
}
