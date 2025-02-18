package repositories;

import models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    boolean isEmailOrUsernameExists(String email, String username) throws SQLException;
    List<User> findAll() throws SQLException;
    Optional<User> findByEmail(String email) throws SQLException;
    Optional<User> findByUsername(String username) throws SQLException;
    Optional<User> findById(Long id) throws SQLException;
    List<User> findAllByLanguage(String language) throws SQLException;
    void update(User entity) throws SQLException;
}
