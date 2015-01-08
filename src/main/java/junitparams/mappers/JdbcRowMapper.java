package junitparams.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Default implementation of {@link junitparams.mappers.RowMapper} retrieving all available columns
 * by {@link java.sql.ResultSet#getObject(int)}.
 */
public class JdbcRowMapper implements RowMapper {

    @Override
    public Object[] map(ResultSet resultSet, int columnCount) throws SQLException {
        Object[] record = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            record[i] = resultSet.getObject(i + 1);
        }
        return record;
    }
}
