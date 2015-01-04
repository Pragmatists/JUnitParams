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
    @DatabaseParameters(sql = "select name, age from persons;",
            url = "jdbc:h2:mem:testdb",
            executor = PersonQueryExecutor.class)
    public void shouldLoadParamsWithAnnotationConfigAndSimpleResultSetMapper(String name, int age) {
        assertThat(name).isNotEmpty();
        assertThat(age).isGreaterThan(0);
    }

    @Test
    @DatabaseParameters(sql = "select name, age from persons;",
            driverClass = "org.h2.Driver", url = "jdbc:h2:mem:testdb", user = "sa", password = "sa",
            executor = PersonQueryExecutor.class, mapper = PersonResultSetMapper.class)
    public void shouldLoadParamsWithAnnotationConfigAndCustomResultSetMapper(Person person) {
        assertThat(person.getName()).isEqualTo(person.getName().toUpperCase());
        assertThat(person.getAge()).isGreaterThan(0);
    }
}
