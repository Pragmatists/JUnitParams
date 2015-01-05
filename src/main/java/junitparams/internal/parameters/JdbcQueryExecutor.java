package junitparams.internal.parameters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcQueryExecutor extends QueryExecutor {

    public JdbcQueryExecutor(ConnectionProperties properties) {
        super(properties);
    }

    @Override
    public Connection getConnection() throws SQLException {
        ConnectionProperties properties = getProperties();
        String driver = properties.driver();
        if (!driver.isEmpty()) {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Could not successfully load driver class: " + driver, e);
            }
        }
        return DriverManager.getConnection(properties.url(), properties.user(), properties.password());
    }

    @Override
    public ResultSet execute(Connection connection, String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }
}
