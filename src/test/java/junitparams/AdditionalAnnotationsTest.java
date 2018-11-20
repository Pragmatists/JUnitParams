package junitparams;

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
public class AdditionalAnnotationsTest {

	@Rule
	public OtherRule rule = new OtherRule();

	@Test
	@Parameters({"test"})
	public void additionalAnnotationTestWithParameters(String param) {
		assertThat(param).isEqualTo("test");
		assertThat(rule.annotations).isNotEmpty();
	}

	@Test
	@Parameters(method = "stringParams")
	public void additionalAnnotationTestWithMethod(String param) {
		assertThat(param).startsWith("stringParam");
		assertThat(rule.annotations).isNotEmpty();
	}

	private String[] stringParams() {
		return new String[] {"stringParam1", "stringParam2", "stringParam3"};
	}

	private class OtherRule implements TestRule {
		private Collection<Annotation> annotations;
		@Override
		public Statement apply(final Statement base, final Description description) {
			this.annotations = description.getAnnotations();
			return base;
		}
	}
}
