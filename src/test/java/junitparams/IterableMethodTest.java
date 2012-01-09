package junitparams;

import static junitparams.JUnitParamsRunner.*;

import java.util.*;

import org.junit.*;
import org.junit.runner.*;

@RunWith(JUnitParamsRunner.class)
public class IterableMethodTest {
    @Test
    @Parameters
    public void shouldHandleIterables(String a) {
    }

    public List<Object[]> parametersForShouldHandleIterables() {
        ArrayList<Object[]> params = new ArrayList<Object[]>();
        params.add($("a"));
        return params;
    }

    @Test
    @Parameters
    public void shouldHandleSimplifedIterables(String a) {
    }

    public List<String> parametersForShouldHandleSimplifedIterables() {
        return Arrays.asList("a");
    }
}