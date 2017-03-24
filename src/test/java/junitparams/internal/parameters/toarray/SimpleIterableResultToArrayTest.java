package junitparams.internal.parameters.toarray;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;


public class SimpleIterableResultToArrayTest {

    @Test
    public void testSimpleIterableObjectArray() throws Exception {
        Object[] actual = convert(
                asList(
                        (Object[]) arr("SNARK", "BOOJUM")
                )
        );

        assertThat(actual)
                .isEqualTo(arr(
                        arr("SNARK"),
                        arr("BOOJUM")
                ));
    }

    @Test
    public void testSimpleIterable() throws Exception {

        Object[] actual = convert(
                asList("SNARK", "BOOJUM"));

        assertThat(actual)
                .isEqualTo(arr(
                        arr("SNARK"),
                        arr("BOOJUM")
                ));
    }

    @Test
    public void testNestedManyIterable() throws Exception {

        Object[] actual = convert(
                asList(
                        asList("SNARK", "BOOJUM"),
                        asList("SNARK", "BOOJUM"),
                        asList("SNARK", "BOOJUM")
                )
        );

        Object[] expected = new Object[]{
                new Object[]{asList("SNARK", "BOOJUM")},
                new Object[]{asList("SNARK", "BOOJUM")},
                new Object[]{asList("SNARK", "BOOJUM")}
        };

        assertThat(actual)
                .isEqualTo(expected);

    }

    @Test
    public void testNestedSingleIterable() throws Exception {

        Object[] actual = convert(
                asList(
                        asList("SNARK", "BOOJUM")
                )
        );

        Object[] expected = new Object[]{
                new Object[]{asList("SNARK", "BOOJUM")}
        };

        assertThat(actual)
                .isEqualTo(expected);
    }

    private static Object[] arr(Object... params) {
        return params;
    }

    private Object[] convert(List result) {
        return new SimpleIterableResultToArray(result).convert();
    }
}
