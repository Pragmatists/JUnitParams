package junitparams.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Retrieves and converts a single database row from a result set into an array of test parameters.
 */
public interface RowMapper {

    /**
     * Maps given result row set into an array.
     * <p>Please note that iteration over result set as well as database resource management are done externally.
     *
     * @param resultSet   a result set representing one database row
     * @param columnCount a number of cloumns available in the given result set
     * @return an array representation of a database row
     * @throws SQLException thrown when operation on the result set failed
     */
    Object[] map(ResultSet resultSet, int columnCount) throws SQLException;
}
