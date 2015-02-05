package junitparams.internal.parameters;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Queries, manages and encapsulates database resources like connection and statement.
 */
public interface Session {

    /**
     * Sets given connection properties to the session.
     *
     * @param properties connection properties
     */
    void setProperties(ConnectionProperties properties);

    /**
     * Executes given SQL select passed in {@link junitparams.DatabaseParameters#sql()} annnotation parameter.
     *
     * @param sql select query fetching test parameters
     * @return result set for given sql
     * @throws SQLException thrown when unable to execute given sql
     */
    ResultSet select(String sql) throws SQLException;

    /**
     * Closes underlying database resources like connection and statement.
     *
     * @throws SQLException thrown when unable to close database resources
     */
    void close() throws SQLException;
}
