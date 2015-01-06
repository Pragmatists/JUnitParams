package junitparams.internal.parameters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;

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
        return getStatement().executeQuery(sql);
    }

    @Override
    public void update(String sql) throws SQLException {
        getStatement().executeUpdate(sql);
    }

    private Statement getStatement() throws SQLException {
        Statement statement = getConnection().createStatement();
        statements.add(statement);
        return statement;
    }

    private Connection getConnection() throws SQLException {
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
