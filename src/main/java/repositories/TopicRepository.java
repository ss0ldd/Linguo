package repositories;

import models.Topic;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TopicRepository extends CrudRepository<Topic> {
    Optional<Topic> findByName(String name) throws SQLException;
    Optional<Topic> findById(Long id) throws SQLException;
    List<Topic> findAll() throws SQLException;
    Long findTopicIdByName(String name) throws SQLException;
}
