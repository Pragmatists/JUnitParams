package junitparams;

import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ParametersForEnumTest {

    private static Set<Fruit> testedFruits = new HashSet<Fruit>();

    @AfterClass
    public static void checkAllFruitsTested() {
        assertThat(testedFruits).contains(Fruit.class.getEnumConstants());
    }

    @Test
    @Parameters(source = Fruit.class)
    public void testAFruit(Fruit fruit) throws Exception {
        testedFruits.add(fruit);
    }

    public enum Fruit {
        APPLE,
        BANANA,
        PEAR,
        PLUM
    }

}
