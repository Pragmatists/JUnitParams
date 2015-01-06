package junitparams.internal.parameters;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface Session {

    void setProperties(ConnectionProperties properties);

    ResultSet select(String sql) throws SQLException;

    void update(String sql) throws SQLException;

    void close() throws SQLException;
}
