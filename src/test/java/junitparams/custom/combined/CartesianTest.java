package junitparams.custom.combined;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.*;

public class CartesianTest {

    @Test
    public void shouldReturnEmptyWhenNoArgumentsPassed() {
        // when
        Object result[] = Cartesian.getCartesianProductOf(null);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnInputWhenOneArgumentPassed() {
        Object[] testArray = new String[]{"AAA", "BBB"};
        List<Object[]> list = singletonList(testArray);

        // when
        Object result[] = Cartesian.getCartesianProductOf(list);

        // then
        assertThat(result).isEqualTo(testArray);
    }

    @Test
    public void shouldReturnProductOfTwoArrays() {
        Object[] testArrayOne = new String[]{"AAA", "BBB"};
        Object[] testArrayTwo = new Integer[]{1, 2};

        List<Object[]> test = new ArrayList<Object[]>();
        test.add(testArrayOne);
        test.add(testArrayTwo);

        Object[] expectedResult = new Object[][]{
                {"AAA", 1}, {"AAA", 2},
                {"BBB", 1}, {"BBB", 2},
        };

        // when
        Object result[] = Cartesian.getCartesianProductOf(test);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    public void shouldReturnProductOfThreeArrays() {
        Object[] testArrayOne = new String[]{"AAA", "BBB"};
        Object[] testArrayTwo = new Integer[]{1, 2};
        Object[] testArrayThree = new String[]{"XXX", "YYY"};

        List<Object[]> test = new ArrayList<Object[]>();
        test.add(testArrayOne);
        test.add(testArrayTwo);
        test.add(testArrayThree);

        Object[] expectedResult = new Object[][]{
                {"AAA", 1, "XXX"}, {"AAA", 1, "YYY"},
                {"AAA", 2, "XXX"}, {"AAA", 2, "YYY"},
                {"BBB", 1, "XXX"}, {"BBB", 1, "YYY"},
                {"BBB", 2, "XXX"}, {"BBB", 2, "YYY"},
        };

        // when
        Object result[] = Cartesian.getCartesianProductOf(test);

        // then
        assertThat(result).isEqualTo(expectedResult);
    }
}
