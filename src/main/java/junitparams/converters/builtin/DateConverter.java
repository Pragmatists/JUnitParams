package junitparams.converters.builtin;

import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter implements ParamConverter<Date> {

	@Override
	public Date convert(Object param, String options) throws ConversionFailedException {
		if (null == param) {
			return null;
		}
		final String textParam = param.toString();
		if (textParam.trim().isEmpty()) {
			return null;
		}
		try {
			final DateFormat format;
			if ((null == options) || options.trim().isEmpty()) {
				format = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
			} else {
				format = new SimpleDateFormat(options);
			}
			return format.parse(textParam);
		} catch (ParseException e) {
			throw new ConversionFailedException("Cannot convert '" + textParam + "' to Date: " + e.getMessage());
		}
	}

}
