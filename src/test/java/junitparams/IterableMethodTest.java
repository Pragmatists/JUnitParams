package junitparams;

import static junitparams.JUnitParamsRunner.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class IterableMethodTest {

    @Test
    @Parameters
    public void shouldHandleIterables(String a) {
        assertThat(a).isEqualTo("a");
    }

    public List<Object[]> parametersForShouldHandleIterables() {
        ArrayList<Object[]> params = new ArrayList<Object[]>();
        params.add($("a"));
        return params;
    }

    @Test
    @Parameters
    public void shouldHandleSimplifedIterables(String a) {
        assertThat(a).isEqualTo("a");
    }

    public List<String> parametersForShouldHandleSimplifedIterables() {
        return Arrays.asList("a");
    }
}