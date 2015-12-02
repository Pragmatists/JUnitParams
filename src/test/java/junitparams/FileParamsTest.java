package junitparams;

import junitparams.mappers.CsvWithHeaderMapper;
import junitparams.usage.person_example.PersonMapper;
import junitparams.usage.person_example.PersonTest.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class FileParamsTest {

    @Test
    @FileParameters("src/test/resources/test.csv")
    public void loadParamsFromFileWithIdentityMapper(int age, String name) {
        assertThat(age).isGreaterThan(0);
    }

    @Test
    @FileParameters(value = "src/test/resources/test.csv", mapper = PersonMapper.class)
    public void loadParamsFromFileWithCustomMapper(Person person) {
        assertThat(person.getAge()).isGreaterThan(0);
    }

    @Test
    @FileParameters("classpath:test.csv")
    public void loadParamsFromFileAtClasspath(int age, String name) {
        assertThat(age).isGreaterThan(0);
    }

    @Test
    @FileParameters("file:src/test/resources/test.csv")
    public void loadParamsFromFileAtFilesystem(int age, String name) {
        assertThat(age).isGreaterThan(0);
    }

    @Test
    @FileParameters(value = "classpath:with_header.csv", mapper = CsvWithHeaderMapper.class)
    public void csvWithHeader(int id, String name) {
        assertThat(id).isGreaterThan(0);
    }

    @Test
    @FileParameters(value = "classpath:with_special_chars.csv", encoding = "UTF-8")
    public void loadParamWithCorrectEncoding(String value) {
        assertThat(value).isEqualTo("åäöÅÄÖ");
    }

    @Test
    @FileParameters(value = "classpath:with_special_chars.csv", encoding = "ISO-8859-1")
    public void loadParamWithWrongEncoding(String value) {
        assertThat(value).isNotEqualTo("åäöÅÄÖ");
    }
}
