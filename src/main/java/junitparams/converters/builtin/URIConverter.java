package junitparams.converters.builtin;

import junitparams.converters.ConversionFailedException;
import junitparams.converters.ParamConverter;

import java.net.URI;
import java.net.URISyntaxException;

public class URIConverter implements ParamConverter<URI> {

	@Override
	public URI convert(Object param, String options) throws ConversionFailedException {
		if (null == param) {
			return null;
		}
		final String textParam = param.toString();
		if (textParam.trim().isEmpty()) {
			return null;
		}
		try {
			return new URI(textParam);
		} catch (URISyntaxException e) {
			throw new ConversionFailedException("Cannot convert '" + textParam + "' to URI: " + e.getMessage());
		}
	}

}
