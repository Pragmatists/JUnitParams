package junitparams.usage.person_example;

import junitparams.internal.parameters.JdbcSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonSession extends JdbcSession {

    @Override
    public ResultSet select(String sql) throws SQLException {
        update(fileContentFromStrem("/init_db.sql"));
        return super.select(sql);
    }

    @Override
    public void close() throws SQLException {
        update(fileContentFromStrem("/destroy_db.sql"));
        super.close();
    }

    private void update(String sql) throws SQLException {
        createStatement(sql).executeUpdate();
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
