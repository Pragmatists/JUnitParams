package junitparams.custom;

import junitparams.CsvParameters;
import junitparams.internal.Utils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.junit.runners.model.FrameworkMethod;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author qiuboboy@qq.com
 * @date 2018-01-09 09:45
 */
public class CsvProviders implements ParametersProvider<CsvParameters> {

    private CsvParameters parameter;
    private BeanUtilsBean beanUtilsBean;
    private ConvertUtilsBean convertUtilsBean;
    private Class<?>[] parameterTypes;

    @Override
    public void initialize(CsvParameters parameter, FrameworkMethod frameworkMethod) {
        this.parameter = parameter;
        convertUtilsBean = new ConvertUtilsBean();
        convertUtilsBean.register(new StringToDateConverter(), Date.class);
        beanUtilsBean = new BeanUtilsBean(convertUtilsBean);
        parameterTypes = frameworkMethod.getMethod().getParameterTypes();
        if (parameterTypes.length == 0) {
            throw new IllegalArgumentException(
                    "test case["
                            + frameworkMethod.getMethod().getDeclaringClass().getName()
                            + "#"
                            + frameworkMethod.getMethod().getName()
                            + "] must have parameters");
        }
    }

    @Override
    public Object[] getParameters() {
        try {
            Reader reader = createProperReader();
            try {
                if (reader == null) {
                    return new Object[0];
                }
                BufferedReader br = new BufferedReader(reader);
                String line;
                List<Object> result = new LinkedList<Object>();
                int lineNo = 0;
                String[] header = null;

                while ((line = br.readLine()) != null) {
                    lineNo++;
                    if (lineNo == 1) {
                        header = Utils.splitAtCommaOrPipe(line);
                    } else {
                        if (!line.trim().equals("")) {
                            result.add(parseLine(header, line, lineNo));
                        }
                    }
                }
                return result.toArray();
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object parseLine(String[] header, String line, int lineNo) throws Exception {
        String[] params = Utils.splitAtCommaOrPipe(line);
        if (parameterTypes.length > 1) {
            if (params.length < parameterTypes.length) {
                throw new RuntimeException(
                        "file:" + parameter.value() + " line:" + lineNo + " wrong format,conent:" + line);
            }
            Object[] result = new Object[parameterTypes.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = convertUtilsBean.convert(params[i], parameterTypes[i]);
            }
            return result;
        }

        Converter converter = convertUtilsBean.lookup(parameterTypes[0]);
        if (converter != null) {
            return converter.convert(parameterTypes[0], params[0]);
        }
        Object bean = parameterTypes[0].newInstance();
        if (params.length != header.length) {
            throw new RuntimeException(
                    "file:" + parameter.value() + " line:" + lineNo + " wrong format,conent:" + line);
        }
        for (int i = 0; i < header.length; i++) {
            beanUtilsBean.setProperty(bean, header[i], params[i]);
        }
        return bean;
    }

    private Reader createProperReader() throws IOException {
        String filepath = parameter.value();
        String encoding = parameter.encoding();

        if (filepath.indexOf(':') < 0) {
            return new FileReader(filepath);
        }

        String protocol = filepath.substring(0, filepath.indexOf(':'));
        String filename = filepath.substring(filepath.indexOf(':') + 1);

        if ("classpath".equals(protocol)) {
            return new InputStreamReader(
                    getClass().getClassLoader().getResourceAsStream(filename), encoding);
        } else if ("file".equals(protocol)) {
            return new InputStreamReader(new FileInputStream(filename), encoding);
        }

        throw new IllegalArgumentException(
                "Unknown file access protocol. Only 'file' and 'classpath' are supported!");
    }

    /**
     * default string to date converter
     */
    private class StringToDateConverter implements Converter {

        public static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";
        public static final String DATE_FORMAT_2 = "yyyy-MM-dd";
        public static final String DATE_FORMAT_3 = "yyyy-MM-dd HH:mm:ss.SSS";

        private List<String> dateFormats = new ArrayList<String>();

        public StringToDateConverter() {
            super();
            addDefaultFormats();
        }

        private void addDefaultFormats() {
            dateFormats.add(DATE_FORMAT_1);
            dateFormats.add(DATE_FORMAT_2);
            dateFormats.add(DATE_FORMAT_3);
        }

        @Override
        public <T> T convert(Class<T> type, Object value) {
            for (String dateFormat : dateFormats) {
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                try {
                    return (T) sdf.parse(value.toString());
                } catch (Exception e) {
                    // ig
                }
            }
            throw new IllegalArgumentException(
                    String.format(
                            "date format convert failure,support format：%s,but input value=[%s]",
                            dateFormats.toString(), value));
        }
    }
}
