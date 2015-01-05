package junitparams.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface RowMapper {

    Object[] map(ResultSet resultSet, int columnCount) throws SQLException;
}
