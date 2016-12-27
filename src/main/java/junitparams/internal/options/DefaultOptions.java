package junitparams.internal.options;

public class DefaultOptions implements OptionsReaderStrategy {

	public static final boolean DEFAULT_ALWAYS_TRIM_STRING_VALUES = true;
	public static final boolean APPLICABLE_FOR_ALL = true;

	@Override
	public boolean getTrimStringParams() {
		return DEFAULT_ALWAYS_TRIM_STRING_VALUES;
	}

	@Override
	public boolean isApplicable() {
		return APPLICABLE_FOR_ALL;
	}
}
