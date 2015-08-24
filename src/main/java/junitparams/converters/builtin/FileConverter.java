package junitparams.converters.builtin;

import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.io.File;

public class FileConverter implements ParamConverter<File> {

	@Override
	public File convert(Object param, String options) throws ConversionFailedException {
		if (null == param) {
			return null;
		}
		final String textParam = param.toString();
		return new File(textParam);
	}

}
