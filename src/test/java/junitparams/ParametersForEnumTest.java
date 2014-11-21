package junitparams;

import junitparams.internal.TestMethod;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link TestMethod#fillResultWithAllParamProviderMethods} that
 * verifies that enumeration values are passed.
 *
 * @author Bob Browning
 */
@RunWith(JUnitParamsRunner.class)
public class ParametersForEnumTest {

    /**
     * Set of fruits remaining to be tested.
     */
    private static Set<Fruit> untested =
            Collections.synchronizedSet(EnumSet.allOf(Fruit.class));

    @AfterClass
    public static void after() {
        assertThat(untested).isEmpty();
    }

    /**
     * Sample enumeration.
     */
    public enum Fruit {
        APPLE,
        BANANA,
        PEAR,
        PLUM
    }

    @Test
    @Parameters(source = Fruit.class)
    public void testAllFruitsTested(Fruit fruit) throws Exception {
        // verify and remove fruit from outstanding expectations
        assertThat(fruit).isIn(untested);
        assertTrue("Failed to remove fruit from expectations",
                untested.remove(fruit));
    }
}
