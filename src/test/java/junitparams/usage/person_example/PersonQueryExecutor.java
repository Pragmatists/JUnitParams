package junitparams.usage.person_example;

import junitparams.internal.parameters.ConnectionProperties;
import junitparams.internal.parameters.JdbcQueryExecutor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PersonQueryExecutor extends JdbcQueryExecutor {

    public PersonQueryExecutor(ConnectionProperties properties) {
        super(properties);
    }

    @Override
    public void executeBefore(Connection connection) throws SQLException {
        String sql = fileContentFromStrem("/init_db.sql");
        executeUpdate(connection, sql);
    }

    @Override
    public void executeAfter(Connection connection) throws SQLException {
        String sql = fileContentFromStrem("/destroy_db.sql");
        executeUpdate(connection, sql);
    }

    private void executeUpdate(Connection connection, String sql) throws SQLException {
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    private String fileContentFromStrem(String resourceName) {
        try {
            InputStream is = getClass().getResourceAsStream(resourceName);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }
                return new String(baos.toByteArray());
            } finally {
                baos.close();
                is.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not successfully read resource: " + resourceName);
        }
    }
}
