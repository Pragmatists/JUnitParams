package junitparams.internal.parameters;

import junitparams.DatabaseParameters;

public abstract class ConnectionPropertiesFactory {

    public static ConnectionProperties of(DatabaseParameters dbParamsAnnotation) {
        return new ConnectionProperties(dbParamsAnnotation.driverClass(), dbParamsAnnotation.url(),
                dbParamsAnnotation.user(), dbParamsAnnotation.password());
    }
}
