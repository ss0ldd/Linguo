package repositories;

import models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryJdbcImpl implements UserRepository{

    private DataSource dataSource;

    private static final String SQL_SELECT_FROM_USERLINGUO = "SELECT * FROM userlinguo";

    public UserRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean isEmailOrUsernameExists(String email, String username) throws SQLException {
        return findByEmail(email).isPresent() || findByUsername(username).isPresent();
    }

    private Optional<User> findUserByColumn(String query, Object columnValue) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            if (columnValue instanceof String) {
                statement.setString(1, (String) columnValue);
            } else if (columnValue instanceof Long) {
                statement.setLong(1, (Long) columnValue);
            } else {
                statement.setObject(1, columnValue);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToUser(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .username(resultSet.getString("username"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .language(resultSet.getString("language"))
                .build();
    }

    private List<User> findUsersByQuery(String query, Object... params) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(mapResultSetToUser(resultSet));
                }
                return result;
            }
        }
    }

    public void executeUpdate(String query, Object... params) throws SQLException {
        if (query == null || query.isEmpty() || params == null) throw new SQLException("Query or params should not be null or empty");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                System.out.println(params[i] + params[i].getClass().getName());
                if (params[i] instanceof String) {
                    statement.setString(i + 1, (String) params[i]);
                } else if (params[i] instanceof Long) {
                    statement.setLong(i + 1, (Long) params[i]);
                } else {
                    statement.setObject(i + 1, params[i]);
                }
            }
            System.out.println("tried executin ");
            statement.executeUpdate();
        }
    }

    @Override
    public List<User> findAll() throws SQLException {
        return findUsersByQuery(SQL_SELECT_FROM_USERLINGUO);
    }

    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        System.out.println("in fundByEmail");
        String query = SQL_SELECT_FROM_USERLINGUO + " WHERE email = ?";
        return findUserByColumn(query, email);
    }

    @Override
    public Optional<User> findByUsername(String username) throws SQLException {
        String query = SQL_SELECT_FROM_USERLINGUO + " WHERE username = ?";
        return findUserByColumn(query, username);
    }

    @Override
    public Optional<User> findById(Long id) throws SQLException {
        String query = SQL_SELECT_FROM_USERLINGUO + " WHERE user_id = ?";
        return findUserByColumn(query, id);
    }

    @Override
    public List<User> findAllByLanguage(String language) throws SQLException {
        String query = SQL_SELECT_FROM_USERLINGUO + " WHERE language = ?";
        return findUsersByQuery(query, language);
    }

    @Override
    public void save(User entity) throws SQLException {
        String query = "INSERT INTO userlinguo(username, email, password, language) VALUES (?, ?, ?, ?)";
        System.out.println("userRepositoryJdbcImpl.save");
        if (entity.getUsername() == null || entity.getEmail() == null || entity.getPassword() == null || entity.getLanguage() == null) {
            throw new IllegalArgumentException("All fields must be non-null");
        }
        executeUpdate(query, entity.getUsername(), entity.getEmail(), entity.getPassword(), entity.getLanguage());
    }

    @Override
    public void update(User entity) throws SQLException {
        String query = "UPDATE userlinguo SET username = ?, email = ?, password = ?, language = ? WHERE user_id = ?";
        System.out.println("userRepositoryJdbcImpl.update");
        executeUpdate(query, entity.getUsername(), entity.getEmail(), entity.getPassword(), entity.getLanguage(), entity.getId());
    }

    @Override
    public void removeById(Long id) throws SQLException {
        String query = "DELETE FROM userlinguo WHERE user_id = ?";
        executeUpdate(query, id);
    }
}
