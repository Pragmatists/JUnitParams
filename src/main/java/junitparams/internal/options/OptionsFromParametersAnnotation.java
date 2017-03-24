package junitparams.internal.options;

import junitparams.Parameters;
import org.junit.runners.model.FrameworkMethod;

public class OptionsFromParametersAnnotation implements OptionsReaderStrategy {

	private final Parameters parametersAnnotation;

	public OptionsFromParametersAnnotation(FrameworkMethod frameworkMethod) {
		parametersAnnotation = frameworkMethod.getAnnotation(Parameters.class);
	}

	@Override
	public boolean getTrimStringParams() {
		return parametersAnnotation.trimStringParams();
	}

	@Override
	public boolean isApplicable() {
		return parametersAnnotation != null;
	}
}
