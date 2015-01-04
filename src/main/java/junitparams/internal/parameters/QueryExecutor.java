package junitparams.internal.parameters;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class QueryExecutor {

    private final ConnectionProperties properties;

    public QueryExecutor(ConnectionProperties properties) {
        this.properties = properties;
    }

    protected ConnectionProperties getProperties() {
        return properties;
    }

    public abstract Connection getConnection() throws SQLException;

    public void executeBefore(Connection connection) throws SQLException {
    }

    public abstract ResultSet execute(Connection connection, String sql) throws SQLException;

    public void executeAfter(Connection connection) throws SQLException {
    }
}
