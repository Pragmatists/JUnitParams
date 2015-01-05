package junitparams.internal.parameters;

import junitparams.DatabaseParameters;
import junitparams.FileParameters;
import junitparams.Parameters;
import junitparams.mappers.RowMapper;
import org.junit.runners.model.FrameworkMethod;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

class ParametersFromDatabase implements ParametrizationStrategy {

    private FrameworkMethod frameworkMethod;

    ParametersFromDatabase(FrameworkMethod frameworkMethod) {
        this.frameworkMethod = frameworkMethod;
    }

    @Override
    public Object[] getParameters() {
        return paramsFromDatabase(frameworkMethod.getAnnotation(DatabaseParameters.class));
    }

    @Override
    public boolean isApplicable() {
        return frameworkMethod.getAnnotation(DatabaseParameters.class) != null
            && frameworkMethod.getAnnotation(Parameters.class) == null
            && frameworkMethod.getAnnotation(FileParameters.class) == null;
    }

    private Object[] paramsFromDatabase(DatabaseParameters dbParamsAnnotation) {
        try {
            QueryExecutor queryExecutor = null;
            Connection connection = null;
            Statement statement = null;
            try {
                ConnectionProperties connectionProperties = ConnectionPropertiesFactory.of(dbParamsAnnotation);
                queryExecutor = dbParamsAnnotation.executor().
                        getDeclaredConstructor(ConnectionProperties.class).
                        newInstance(connectionProperties);
                connection = queryExecutor.getConnection();
                queryExecutor.executeBefore(connection);
                ResultSet resultSet = queryExecutor.execute(connection, dbParamsAnnotation.sql());
                statement = resultSet.getStatement();
                RowMapper mapper = dbParamsAnnotation.mapper().newInstance();
                return mapRecords(resultSet, mapper);
            } finally {
                if (statement != null) {
                    statement.close();
                }
                if (connection != null) {
                    queryExecutor.executeAfter(connection);
                    connection.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Could not successfully read parameters from database: " + dbParamsAnnotation.url(), e);
        }
    }

    private Object[] mapRecords(ResultSet resultSet, RowMapper mapper) throws SQLException {
        List<Object[]> records = new LinkedList<Object[]>();
        int columnCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            Object[] record = mapper.map(resultSet, columnCount);
            records.add(record);
        }
        return records.toArray();
    }
}