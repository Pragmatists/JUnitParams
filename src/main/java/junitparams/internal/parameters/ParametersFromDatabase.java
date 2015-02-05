package junitparams.internal.parameters;

import junitparams.DatabaseParameters;
import junitparams.FileParameters;
import junitparams.Parameters;
import junitparams.mappers.RowMapper;
import org.junit.runners.model.FrameworkMethod;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

class ParametersFromDatabase implements ParametrizationStrategy {

    private FrameworkMethod frameworkMethod;

    private class DatabaseParametersFactory {

        DatabaseParameters parameters;

        DatabaseParametersFactory(DatabaseParameters parameters) {
            this.parameters = parameters;
        }

        Session session() {
            Session session = newInstance(parameters.session());
            ConnectionProperties properties = ConnectionProperties.Factory.of(parameters);
            session.setProperties(properties);
            return session;
        }

        RowMapper mapper() {
            return newInstance(parameters.mapper());
        }

        <T> T newInstance(Class<T> clazz) {
            try {
                return clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Could not successfully create new instance of: " + clazz, e);
            }
        }
    }

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
            DatabaseParametersFactory dbParamsFactory = new DatabaseParametersFactory(dbParamsAnnotation);
            Session session = dbParamsFactory.session();
            try {
                ResultSet resultSet = session.select(dbParamsAnnotation.sql());
                return mapRecords(resultSet, dbParamsFactory.mapper());
            } finally {
                session.close();
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