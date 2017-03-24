package junitparams.internal.options;

import junitparams.custom.combined.CombinedParameters;
import org.junit.runners.model.FrameworkMethod;

public class OptionsFromCombinedParametersAnnotation implements OptionsReaderStrategy {

	private final CombinedParameters parametersAnnotation;

	public OptionsFromCombinedParametersAnnotation(FrameworkMethod frameworkMethod) {
		parametersAnnotation = frameworkMethod.getAnnotation(CombinedParameters.class);
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
