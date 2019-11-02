package ua.nure.kn.akhremenko.usermanagement.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionFactoryImpl implements ConnectionFactory {

    private String driver;
    private String url;
    private String user;
    private String password;

    public ConnectionFactoryImpl(final String driver, final String url, final String user, final String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public ConnectionFactoryImpl(Properties properties) {
        this.driver = properties.getProperty("connection.driver");
        this.url = properties.getProperty("connection.url");
        this.user = properties.getProperty("connection.user");
        this.password = properties.getProperty("connection.password");
    }

    @Override
    public Connection createConnection() throws DatabaseException {

        try {
            Class.forName(this.driver);
            return DriverManager.getConnection(this.url, this.user, this.password);
        } catch (Exception e) {
            throw new DatabaseException(e);
        }
    }
}
