package junitparams.usage.person_example;

import junitparams.mappers.CsvWithHeaderMapper;
import junitparams.mappers.ResultSetMapper;
import junitparams.mappers.SimpleResultSetMapper;

import java.io.Reader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PersonResultSetMapper extends SimpleResultSetMapper {

    @Override
    public Object[] map(ResultSet resultSet) throws SQLException {
        Object[] records = super.map(resultSet);
        List<Object[]> result = new ArrayList<Object[]>(records.length);
        for (Object record : records) {
            Object[] columns = (Object[]) record;
            result.add(new Object[] { columns[0].toString().toUpperCase(), columns[1] });
        }
        return result.toArray();
    }
}