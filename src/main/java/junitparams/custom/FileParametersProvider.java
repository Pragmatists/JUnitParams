package junitparams.custom;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import junitparams.FileParameters;
import junitparams.mappers.DataMapper;

public class FileParametersProvider implements ParametersProvider<FileParameters> {

    private FileParameters fileParameters;

    @Override
    public void initialize(FileParameters fileParameters) {
        this.fileParameters = fileParameters;
    }

    @Override
    public Object[] provideParameters() {
        return paramsFromFile(fileParameters);
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
