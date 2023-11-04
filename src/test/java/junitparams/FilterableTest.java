package junitparams;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;

import static org.assertj.core.api.Assertions.*;

public class FilterableTest {

    @Test
    public void shouldRunAllTests() throws Exception {
        Request request = Request.aClass(SampleTestCase.class);

        Result result = new JUnitCore().run(request);

        assertThat(result.getRunCount()).isEqualTo(3);
    }

    @Test
    public void shouldRunSingleTestWithoutParameters() throws Exception {
        Request request = Request.method(SampleTestCase.class, "firstTestMethod");

        Result result = new JUnitCore().run(request);

        assertThat(result.getRunCount()).isEqualTo(1);
    }

    @Test
    public void shouldRunParametrisedTest() throws Exception {
        Request request = Request.method(SampleTestCase.class, "secondTestMethod");

        Result result = new JUnitCore().run(request);

        assertThat(result.getRunCount()).isEqualTo(2);
    }

    @Test
    public void shouldReturnOneDescriptionForSimpleTestCase() throws Exception {
        Request request = Request.method(SampleTestCase.class, "firstTestMethod");

        Runner runner = request.getRunner();
        Description description = runner.getDescription();

        assertThat(runner.testCount() == 1);
        for (Description desc: description.getChildren())
            if (desc.getDisplayName() == "firstTestMethod")
                assertThat(desc.getChildren()).hasSize(0);
    }

    @Test
    public void shouldReturnParametrizedDescriptionsForParametrizedTestCase() throws Exception {
        Request request = Request.method(SampleTestCase.class, "secondTestMethod");

        Runner runner = request.getRunner();
        Description description = runner.getDescription();

        assertThat(runner.testCount() == 1);
        for (Description desc: description.getChildren())
            if (desc.getDisplayName() == "secondTestMethod")
                assertThat(desc.getChildren()).hasSize(2);
    }
}
