package junitparams.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SimpleResultSetMapper implements ResultSetMapper {

    @Override
    public Object[] map(ResultSet resultSet) throws SQLException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        List<Object[]> records = new LinkedList<Object[]>();
        while (resultSet.next()) {
            Object[] record = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                record[i] = resultSet.getObject(i + 1);
            }
            records.add(record);
        }
        return records.toArray();
    }
}
