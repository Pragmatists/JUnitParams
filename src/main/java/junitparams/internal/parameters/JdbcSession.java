package junitparams.internal.parameters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Default implementation of {@link junitparams.internal.parameters.Session}
 * using prepared statements for querying database and opening new connection for each session (no connection pooling).
 */
public class JdbcSession implements Session {

    private ConnectionProperties properties;

    private Connection connection;

    private Collection<Statement> statements = new LinkedList<Statement>();

    @Override
    public void setProperties(ConnectionProperties properties) {
        this.properties = properties;
    }

    @Override
    public ResultSet select(String sql) throws SQLException {
        return createStatement(sql).executeQuery();
    }

    /**
     * Creates prepared statement for given sql.
     *
     * @param sql an SQL statement
     * @return a new prepared statement for given sql
     * @throws SQLException thrown when unable to create prepared statement
     */
    protected PreparedStatement createStatement(String sql) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement(sql);
        statements.add(statement);
        return statement;
    }

    /**
     * Creates new or returns existing database connection.
     * <p>Please note that this method does not use connection pool.
     *
     * @return a database connection created on first call of this method
     * @throws SQLException thrown when unable to get connection
     */
    protected Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }
        String driver = properties.driver();
        if (!driver.isEmpty()) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Could not successfully load driver class: " + driver, e);
            }
        }
        connection = DriverManager.getConnection(properties.url(), properties.user(), properties.password());
        return connection;
    }

    @Override
    public void close() throws SQLException {
        for (Statement statement : statements) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
