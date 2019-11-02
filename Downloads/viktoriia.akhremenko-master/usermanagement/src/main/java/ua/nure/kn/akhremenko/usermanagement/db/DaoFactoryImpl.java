package ua.nure.kn.akhremenko.usermanagement.db;

public class DaoFactoryImpl extends DaoFactory {

    @Override
    public UserDao getUserDao() {
        try {
            Class userDaoClass = Class.forName(properties.getProperty(USER_DAO_CLASS_KEY));

            UserDao userDao = (UserDao) userDaoClass.newInstance();
            userDao.setConnectionFactory(getConnectionFactory());

            return userDao;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
