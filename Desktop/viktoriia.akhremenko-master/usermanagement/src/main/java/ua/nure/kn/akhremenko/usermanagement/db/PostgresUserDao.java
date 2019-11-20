package ua.nure.kn.akhremenko.usermanagement.db;

import ua.nure.kn.akhremenko.usermanagement.User;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class PostgresUserDao implements UserDao {

    private static final String SELECT_ALL_QUERY = "SELECT id, first_name, last_name, date_of_birth FROM users;";
    private static final String SELECT_BY_ID_QUERY = "SELECT id, first_name, last_name, date_of_birth FROM users WHERE id = (?);";
    private static final String SELECT_BY_FIRST_AND_LAST_NAME_QUERY = "SELECT id, first_name, last_name, date_of_birth FROM users WHERE first_name ~ (?) AND last_name ~ (?);";
    private static final String INSERT_QUERY = "INSERT INTO users (first_name, last_name, date_of_birth) VALUES (?, ?, ?) RETURNING id;";
    private static final String UPDATE_QUERY = "UPDATE users SET first_name = (?), last_name = (?), date_of_birth = (?) WHERE id = (?);";
    private static final String DELETE_QUERY = "DELETE FROM users WHERE id = (?);";
    private ConnectionFactory connectionFactory;

    public PostgresUserDao() {
    }

    public PostgresUserDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    public User create(User user) throws DatabaseException {
        try {
            Connection connection = this.connectionFactory.createConnection();

            PreparedStatement statement = connection.prepareStatement(INSERT_QUERY);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setDate(3, Date.valueOf(user.getDateOfBirth()));

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            if (resultSet.next()) {
                Long id = resultSet.getLong(1);
                user.setId(id);
            } else {
                throw new DatabaseException("Something went wrong when creating a new user on database.");
            }

            statement.close();
            connection.close();

            return user;

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public User find(Long id) throws DatabaseException {
        try {
            Connection connection = this.connectionFactory.createConnection();

            PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long recordId = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();

                if (resultSet.next()) {
                    throw new DatabaseException("There are more than one user with specified id in the database.");
                }

                resultSet.close();
                statement.close();
                connection.close();

                return new User(recordId, firstName, lastName, dateOfBirth);

            }

            throw new DatabaseException("User not found.");

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Collection<User> find(String firstName, String lastName) throws DatabaseException {
        try {
            Connection connection = connectionFactory.createConnection();
            PreparedStatement statement = connection.prepareStatement(SELECT_BY_FIRST_AND_LAST_NAME_QUERY);

            statement.setString(1, firstName);
            statement.setString(2, lastName);

            ResultSet resultSet = statement.executeQuery();

            Collection<User> users = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong(1);
                String first = resultSet.getString(2);
                String last = resultSet.getString(3);
                LocalDate dateOfBirth = resultSet.getDate(4).toLocalDate();

                users.add(new User(id, first, last, dateOfBirth));
            }

            resultSet.close();
            statement.close();
            connection.close();

            return users;

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Collection<User> findAll() throws DatabaseException {
        try {
            Collection<User> result = new LinkedList<>();

            Connection connection = this.connectionFactory.createConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_QUERY);

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                LocalDate dateOfBirth = resultSet.getDate("date_of_birth").toLocalDate();

                User user = new User(id, firstName, lastName, dateOfBirth);
                result.add(user);
            }

            resultSet.close();
            statement.close();
            connection.close();

            return result;

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void update(User user) throws DatabaseException {
        try {
            Connection connection = this.connectionFactory.createConnection();

            PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setDate(3, Date.valueOf(user.getDateOfBirth()));
            statement.setLong(4, user.getId());

            int n = statement.executeUpdate();

            if (n != 1) {
                throw new DatabaseException("Num of updated rows: " + n);
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public void delete(Long id) throws DatabaseException {
        try {
            Connection connection = this.connectionFactory.createConnection();

            PreparedStatement statement = connection.prepareStatement(DELETE_QUERY);
            statement.setLong(1, id);

            int n = statement.executeUpdate();

            if (n != 1) {
                throw new DatabaseException("Num of deleted rows: " + n);
            }

            statement.close();
            connection.close();

        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
