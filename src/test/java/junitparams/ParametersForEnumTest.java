package junitparams;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.EnumSet;

import junitparams.internal.TestMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link TestMethod#fillResultWithAllParamProviderMethods} that verifies that enumeration values are passed.
 *
 * @author Bob Browning
 */
@RunWith(JUnitParamsRunner.class)
public class ParametersForEnumTest {

  /**
   * Ensures that the test is thread-safe.
   */
  private static ThreadLocal<EnumSet<Fruit>> threadUntested = new ThreadLocal<EnumSet<Fruit>>() {
    @Override
    protected EnumSet<Fruit> initialValue() {
      return EnumSet.allOf(Fruit.class);
    }
  };

  @AfterClass
  public static void after() {
    try {
      assertThat(threadUntested.get()).isEmpty();
    } finally {
      threadUntested.remove();
    }
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
    EnumSet<Fruit> untested = threadUntested.get();
    // verify and remove fruit from outstanding expectations
    assertThat(fruit).isIn(untested);
    assertTrue("Failed to remove fruit from expectations", untested.remove(fruit));
  }
}
