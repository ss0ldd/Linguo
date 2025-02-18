package repositories;

import models.Question;
import models.Topic;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends CrudRepository<Question> {
    List<Question> findAllByUserId(Long id) throws SQLException;
    Optional<Question> findById(Long id) throws SQLException;
    List<Question> findByTopicId(Long id) throws SQLException;
    List<Question> findAll() throws SQLException;
    Long saveAndReturnId(Question question) throws SQLException;
}
