package junitparams;

import junitparams.custom.CsvProviders;
import junitparams.custom.CustomParameters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes that parameters (converted to javabean) for a annotated test method  be taken from an csv resource .
 *
 * <pre class="code"><code>
 *
 * {@literal @}Test
 * {@literal @}CsvParameters(value = "classpath:with_header1.csv")
 * public void loadParamsFromAnyCsvWithConverter(Person person) {
 *
 * }
 *
 * </code></pre>
 *
 * @author qiuboboy@qq.com
 */
@Retention(RetentionPolicy.RUNTIME)
@CustomParameters(provider = CsvProviders.class)
public @interface CsvParameters {

    /**
     * File name (with full path) of the file with data.
     * <p>
     * <p>support prefix: classpath、file
     */
    String value();

    String encoding() default "UTF-8";
}
