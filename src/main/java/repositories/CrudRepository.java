package repositories;

import java.sql.SQLException;

public interface CrudRepository<T> {
    void save(T entity) throws SQLException;
    void removeById(Long id) throws SQLException;
}
