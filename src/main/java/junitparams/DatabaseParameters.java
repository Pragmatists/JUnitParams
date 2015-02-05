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

    /**
     * SQL select to execute on a given database, e.g. {@code select name, age from persons};
     */
    String sql();

    /**
     * JDBC connection URL pointing to the database, e.g. {@code jdbc:h2:mem:testdb}.
     * <p>Please note that other parameters like credentials, connection or database properties can also be passed in the URL.
     */
    String url();

    /**
     * JDBC driver class (optional), e.g. {@code org.h2.Driver}.
     * <p>Drivers supporting JDBC 4.0 should be recognized automatically.
     */
    String driver() default "";

    /**
     * Username (optional) accessing the database.
     * <p>Username can be empty if no authentication required or can be passed in the {@link #url}.
     */
    String user() default "";

    /**
     * Password (optional) for a user accessing the database.
     * <p>Password can be empty if no authentication required or can be passed in the {@link #url}.
     */
    String password() default "";

    /**
     * JDBC session (optional) for a given {@link #sql} query.
     * <p>Default implementation {@link junitparams.internal.parameters.JdbcSession} can be overridden
     * to handle a logic of query execution or session management in a different way.
     */
    Class<? extends Session> session() default JdbcSession.class;

    /**
     * JDBC row mapper (optional) responsible for retrieving and converting column values from a result set.
     * <p>Default implementation {@link junitparams.mappers.JdbcRowMapper} can be overridden
     * to customize database row mapping.
     */
    Class<? extends RowMapper> mapper() default JdbcRowMapper.class;
}
