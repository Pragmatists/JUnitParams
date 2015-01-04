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
        String driverClass = properties.driverClass();
        if (!driverClass.isEmpty()) {
            try {
                Class.forName(driverClass);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Could not successfully load driver class: " + driverClass, e);
            }
        }
        return DriverManager.getConnection(properties.url(), properties.user(), properties.password());
    }

    @Override
    public ResultSet execute(Connection connection, String sql) throws SQLException {
        return connection.createStatement().executeQuery(sql);
    }
}
