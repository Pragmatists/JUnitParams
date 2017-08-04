package junitparams;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.mappers.CsvWithHeaderMapper;
import junitparams.usage.person_example.PersonMapper;
import junitparams.usage.person_example.PersonTest.Person;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.*;

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

    @Test
    @FileParameters(value = "src/test/resources/ISO-8859-1.csv", encoding = "iso-8859-1")
    public void loadFromAnsi(String fromFile) throws IOException {
        String expectedLine = firstLineFromFile("src/test/resources/ISO-8859-1.csv", "iso-8859-1");

        assertThat(fromFile.getBytes("iso-8859-1")).isEqualTo(expectedLine.getBytes("iso-8859-1"));
    }

    @Test
    @FileParameters(value = "src/test/resources/x-UTF-16LE-BOM.csv", encoding = "utf-16le")
    public void loadFromUtf16Le(String fromFile) throws IOException {
        String expectedLine = firstLineFromFile("src/test/resources/x-UTF-16LE-BOM.csv", "utf-16le");

        assertThat(fromFile.getBytes("utf-16le")).isEqualTo(expectedLine.getBytes("utf-16le"));
    }

    private String firstLineFromFile(String filePath, String encoding) throws FileNotFoundException {
        Scanner scanner = new Scanner(new InputStreamReader(new FileInputStream(filePath), Charset.forName(encoding)));
        return scanner.nextLine();
    }
}
