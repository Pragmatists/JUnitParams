package junitparams.usage.person_example;

import junitparams.mappers.ResultSetRowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper extends ResultSetRowMapper {

    @Override
    public Object[] map(ResultSet resultSet, int columnCount) throws SQLException {
        String name = resultSet.getString("name");
        int age = resultSet.getInt("age");
        return new Object[] { name.toUpperCase(), -age };
    }
}