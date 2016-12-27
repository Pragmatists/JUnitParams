package junitparams.internal.options;

import org.junit.runners.model.FrameworkMethod;

import java.util.List;

import static java.util.Arrays.asList;

public class OptionsReader {
	private static final DefaultOptions DEFAULT_OPTIONS = new DefaultOptions();
	private final List<OptionsReaderStrategy> strategies;

	public OptionsReader(FrameworkMethod frameworkMethod) {
		strategies = asList(
				new OptionsFromParametersAnnotation(frameworkMethod),
				new OptionsFromCombinedParametersAnnotation(frameworkMethod)
		);
	}

	public boolean getTrimStringParams() {
		for (OptionsReaderStrategy strategy : strategies) {
			if (strategy.isApplicable()) {
				return strategy.getTrimStringParams();
			}
		}
		return DEFAULT_OPTIONS.getTrimStringParams();
	}
}
