package junitparams;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class IterableMethodTest {

    @Test
    @Parameters
    public void shouldHandleIterables(String a) {
        assertThat(a).isEqualTo("a");
    }

    public List<Object[]> parametersForShouldHandleIterables() {
        ArrayList<Object[]> params = new ArrayList<Object[]>();
        params.add(new Object[]{"a"});
        return params;
    }

    @Test
    @Parameters
    public void shouldHandleSimplifiedIterables(String a) {
        assertThat(a).isEqualTo("a");
    }

    public List<String> parametersForShouldHandleSimplifiedIterables() {
        return Arrays.asList("a");
    }
}