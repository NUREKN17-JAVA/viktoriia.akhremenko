package ua.nure.kn.akhremenko.usermanagement.db;

import java.io.IOException;
import java.util.Properties;

public abstract class DaoFactory {

    private static DaoFactory instance;

    private static final String DAO_FACTORY = "dao.factory";
    static final String USER_DAO_CLASS_KEY = "dao.usermanagement.db.UserDao";
    private static final String PROPERTIES_RESOURCE = "settings.properties";

    static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(DaoFactory.class.getClassLoader()
                    .getResourceAsStream(PROPERTIES_RESOURCE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    DaoFactory() {
    }

    public static void init(Properties props) {
        properties = props;
        instance = null;
    }

    public static synchronized DaoFactory getInstance() {
        if (instance == null) {
            try {
                Class factoryClass = Class.forName(properties.getProperty(DAO_FACTORY));
                instance = (DaoFactory) factoryClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return instance;
    }

    ConnectionFactory getConnectionFactory() {
        return new ConnectionFactoryImpl(properties);
    }

    public abstract UserDao getUserDao();

}
