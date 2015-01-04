package junitparams.internal.parameters;

public class ConnectionProperties {

    private final String driverClass;
    private final String url;
    private final String user;
    private final String password;

    ConnectionProperties(String driverClass, String url, String user, String password) {
        this.driverClass = driverClass;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String driverClass() {
        return driverClass;
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
