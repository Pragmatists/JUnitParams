package junitparams.internal.parameters;

import junitparams.DatabaseParameters;

public class ConnectionProperties {

    private final String driver;
    private final String url;
    private final String user;
    private final String password;

    static class Factory {
        static ConnectionProperties of(DatabaseParameters dbParamsAnnotation) {
            return new ConnectionProperties(dbParamsAnnotation.driver(), dbParamsAnnotation.url(),
                    dbParamsAnnotation.user(), dbParamsAnnotation.password());
        }
    }

    private ConnectionProperties(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String driver() {
        return driver;
    }

    public String url() {
        return url;
    }

    public String user() {
        return user;
    }

    public String password() {
        return password;
    }
}
