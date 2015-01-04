package junitparams.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetMapper {

    Object[] map(ResultSet resultSet) throws SQLException;
}
