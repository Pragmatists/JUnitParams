package junitparams;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;
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
        Request request = requestSingleMethodRun(SampleTestCase.class, "firstTestMethod");

        Result result = new JUnitCore().run(request);

        assertThat(result.getRunCount()).isEqualTo(1);
    }

    @Test
    public void shouldRunParametrisedTest() throws Exception {
        Request request = requestSingleMethodRun(SampleTestCase.class, "secondTestMethod");

        Result result = new JUnitCore().run(request);

        assertThat(result.getRunCount()).isEqualTo(2);
    }

    @Test
    public void shouldReturnOneDescriptionForSimpleTestCase() throws Exception {
        Request request = requestSingleMethodRun(SampleTestCase.class, "firstTestMethod");

        Description description = request.getRunner().getDescription();

        assertThat(description.getChildren()).hasSize(1);
        assertThat(description.getChildren().get(0).getChildren()).hasSize(0);
    }

    @Test
    public void shouldReturnParametrizedDescriptionsForParametrizedTestCase() throws Exception {
        Request request = requestSingleMethodRun(SampleTestCase.class, "secondTestMethod");

        Description description = request.getRunner().getDescription();

        assertThat(description.getChildren()).hasSize(1);
        assertThat(description.getChildren().get(0).getChildren()).hasSize(2);
    }

    private Request requestSingleMethodRun(Class<SampleTestCase> clazz, String methodName) {
        return Request.aClass(clazz).filterWith(new SingleMethodFilter(methodName));
    }

    private static class SingleMethodFilter extends Filter {
        private final String methodName;

        public SingleMethodFilter(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public boolean shouldRun(Description description) {
            return description.getDisplayName().contains(methodName);
        }

        @Override
        public String describe() {
            return methodName;
        }
    }
}
