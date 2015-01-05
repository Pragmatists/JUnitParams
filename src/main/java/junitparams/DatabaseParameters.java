package junitparams;

import junitparams.internal.parameters.QueryExecutor;
import junitparams.internal.parameters.JdbcQueryExecutor;
import junitparams.mappers.ResultSetMapper;
import junitparams.mappers.SimpleResultSetMapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes that parameters for a annotated test method should be taken from a database.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseParameters {

    String sql();

    String url();

    /**
     * Drivers supporting JDBC 4.0 does not need to call Class.forName()
     */
    String driver() default "";

    String user() default "";

    String password() default "";

    Class<? extends QueryExecutor> executor() default JdbcQueryExecutor.class;

    Class<? extends ResultSetMapper> mapper() default SimpleResultSetMapper.class;
}
