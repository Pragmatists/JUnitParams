package junitparams.converters.builtin;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.math.BigInteger;

public class BigIntegerPropertyEditor extends PropertyEditorSupport {

	@Override
	public String getAsText() {
		final Object obj = getValue();
		if (null == obj) {
			return "";
		} else if (obj instanceof BigInteger) {
			return ((BigInteger) obj).toString();
		}
		return null;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if ((null == text) || text.trim().isEmpty()) {
			setValue(null);
		} else {
			try {
				BigInteger num = new BigInteger(text);
				setValue(num);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(text, e);
			}
		}
	}

	@Override
	public String getJavaInitializationString() {
		final Object obj = getValue();
		if (null == obj) {
			return "null";
		} else if (obj instanceof BigInteger) {
			return "new java.math.BigInteger(\"" + obj + "\")";
		}
		throw new IllegalStateException("bad value type: " + obj.getClass());
	}

}
