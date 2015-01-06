package junitparams;

import junitparams.internal.parameters.JdbcSession;
import junitparams.internal.parameters.Session;
import junitparams.mappers.JdbcRowMapper;
import junitparams.mappers.RowMapper;

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

    Class<? extends Session> session() default JdbcSession.class;

    Class<? extends RowMapper> mapper() default JdbcRowMapper.class;
}
