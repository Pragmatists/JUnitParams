package junitparams.rules;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runners.model.Statement;

import java.lang.annotation.Annotation;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class MethodAnnotationInfoTest {

    @Rule
    public final TestDescription testDescription = new TestDescription();

    @Test
    @Parameters({""})
    public void annotationInfoInDescriptionPresent(String n) {
        assertThat(testDescription.getAnnotations()).isNotEmpty();
    }

    public static class TestDescription implements TestRule {

        private Description description;

        @Override
        public Statement apply(Statement statement, Description description) {
            this.description = description;
            return statement;
        }

        Collection<Annotation> getAnnotations() {
            return description.getAnnotations();
        }
    }
}
