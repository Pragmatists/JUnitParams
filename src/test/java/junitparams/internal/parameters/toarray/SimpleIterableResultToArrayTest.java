package junitparams.internal.parameters.toarray;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

import static junitparams.JUnitParamsRunner.$;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * Tests for {@link SimpleIterableResultToArray}.
 */
public class SimpleIterableResultToArrayTest {

    private static <T> ArrayList<T> newArrayList(T... args) {
        ArrayList<T> list = new ArrayList<T>();
        Collections.addAll(list, args);
        return list;
    }

    private ArrayList<String> strings;

    @Before
    public void setUp() throws Exception {
        strings = newArrayList("SNARK", "BOOJUM");
    }

    @Test
    public void testSimpleIterableObjectArray() throws Exception {
        Object[] actual = new SimpleIterableResultToArray(
                newArrayList((Object[]) $("SNARK", "BOOJUM"))
        ).convert();

        assertThat(actual)
                .isEqualTo($(
                        $("SNARK"),
                        $("BOOJUM")
                ));
    }

    @Test
    public void testSimpleIterable() throws Exception {
        Object[] actual = new SimpleIterableResultToArray(strings).convert();
        assertThat(actual)
                .isEqualTo($(
                        $("SNARK"),
                        $("BOOJUM")
                ));
    }

    @Test
    public void testNestedManyIterable() throws Exception {
        @SuppressWarnings("unchecked") ArrayList<ArrayList<String>> result =
                newArrayList(strings, strings, strings);

        Object[] actual = new SimpleIterableResultToArray(result).convert();
        Object[] expected = new Object[]{
                new Object[]{strings},
                new Object[]{strings},
                new Object[]{strings}
        };
        assertThat(actual)
                .isEqualTo(expected);


    }

    @Test
    public void testNestedSingleIterable() throws Exception {
        @SuppressWarnings("unchecked") ArrayList<ArrayList<String>> result =
                newArrayList(strings);

        Object[] actual = new SimpleIterableResultToArray(result).convert();
        Object[] expected = new Object[]{new Object[]{strings}};
        assertThat(actual)
                .isEqualTo(expected);
    }
}
