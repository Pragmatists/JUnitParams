package junitparams.internal.parameters;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.runners.model.FrameworkMethod;

import junitparams.FileParameters;
import junitparams.Parameters;
import junitparams.mappers.DataMapper;

class ParametersFromFile implements ParametrizationStrategy {
    private FrameworkMethod frameworkMethod;

    ParametersFromFile(FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;
    }

    @Override
    public Object[] getParameters() {
        return paramsFromFile(frameworkMethod.getAnnotation(FileParameters.class));
    }

    @Override
    public boolean isApplicable() {
        return frameworkMethod.getAnnotation(Parameters.class) == null &&
                frameworkMethod.getAnnotation(FileParameters.class) != null;
    }

    private Object[] paramsFromFile(FileParameters fileParametersAnnotation) {
        try {
            Reader reader = createProperReader(fileParametersAnnotation);
            DataMapper mapper = fileParametersAnnotation.mapper().newInstance();
            try {
                return mapper.map(reader);
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Could not successfully read parameters from file: " + fileParametersAnnotation.value(), e);
        }
    }

    private Reader createProperReader(FileParameters fileParametersAnnotation) throws IOException {
        String filepath = fileParametersAnnotation.value();
        String encoding = fileParametersAnnotation.encoding();

        if (filepath.indexOf(':') < 0) {
            return new FileReader(filepath);
        }

        String protocol = filepath.substring(0, filepath.indexOf(':'));
        String filename = filepath.substring(filepath.indexOf(':') + 1);

        if ("classpath".equals(protocol)) {
            return new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename), encoding);
        } else if ("file".equals(protocol)) {
            return new InputStreamReader(new FileInputStream(filename), encoding);
        }

        throw new IllegalArgumentException("Unknown file access protocol. Only 'file' and 'classpath' are supported!");
    }
}
