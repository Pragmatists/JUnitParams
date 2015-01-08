package junitparams.internal.parameters;

import junitparams.DatabaseParameters;

/**
 * JDBC connection properties.
 */
public class ConnectionProperties {

    private final String url;
    private final String driver;
    private final String user;
    private final String password;

    static class Factory {
        static ConnectionProperties of(DatabaseParameters dbParamsAnnotation) {
            return new ConnectionProperties(dbParamsAnnotation.url(), dbParamsAnnotation.driver(),
                    dbParamsAnnotation.user(), dbParamsAnnotation.password());
        }
    }

    private ConnectionProperties(String url, String driver, String user, String password) {
        this.url = url;
        this.driver = driver;
        this.user = user;
        this.password = password;
    }

    /**
     * JDBC connection URL pointing to the database, e.g. {@code jdbc:h2:mem:testdb}.
     * <p>Please note that other parameters like credentials, connection or database properties can also be passed in the URL.
     *
     * @return connection URL
     */
    public String url() {
        return url;
    }

    /**
     * JDBC driver class (optional), e.g. {@code org.h2.Driver}.
     * <p>Drivers supporting JDBC 4.0 should be recognized automatically.
     *
     * @return driver class
     */
    public String driver() {
        return driver;
    }

    /**
     * Username (optional) accessing the database.
     * <p>Username can be empty if no authentication required or can be passed in the {@link #url()}.
     *
     * @return username
     */
    public String user() {
        return user;
    }

    /**
     * Password (optional) for a user accessing the database.
     * <p>Password can be empty if no authentication required or can be passed in the {@link #url()}.
     *
     * @return password
     */
    public String password() {
        return password;
    }
}
