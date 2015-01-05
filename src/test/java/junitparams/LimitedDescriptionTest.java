package junitparams;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;

import static org.assertj.core.api.Assertions.assertThat;

public class LimitedDescriptionTest {

    @Test
    public void shouldReturnOneDescriptionForSimpleTestCase() throws Exception {
        Description description = runTest(SampleTestCase.class, "firstTestMethod");

        assertThat(description.getChildren()).hasSize(1);
    }

    @Test
    public void shouldReturnMethodNameForSimpleTestCase() throws Exception {
        Description description = runTest(SampleTestCase.class, "firstTestMethod");

        assertThat(description.getChildren()).extracting("methodName").containsExactly("firstTestMethod");
    }

    @Test
    public void shouldReturnOneDescriptionForParametrizedTestCase() throws Exception {
        Description description = runTest(SampleTestCase.class, "secondTestMethod");

        assertThat(description.getChildren()).hasSize(1);
    }

    @Test
    public void shouldReturnParametrizedDescriptionsForParametrizedTestCase() throws Exception {
        Description description = runTest(SampleTestCase.class, "secondTestMethod");

        assertThat(description.getChildren()).hasSize(1);
        assertThat(description.getChildren().get(0).getChildren()).hasSize(2);
    }

    @Test
    public void shouldReturnAllDescriptionsForWholeTestClass() throws Exception {
        Description description = runTest(SampleTestCase.class);

        assertThat(description.getChildren()).hasSize(2);
    }

    private Description runTest(Class<SampleTestCase> clazz, String methodName) {
        Request request = Request.method(clazz, methodName);
        return runTest(request);
    }

    private Description runTest(Class<SampleTestCase> clazz) {
        Request request = Request.aClass(clazz);
        return runTest(request);
    }

    private Description runTest(Request request) {
        new JUnitCore().run(request);
        return request.getRunner().getDescription();
    }
}
