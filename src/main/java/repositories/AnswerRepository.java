package repositories;

import models.Answer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer> {
    List<Answer> findAll() throws SQLException;
    List<Answer> findByQuestionId(Long id) throws SQLException;
    Optional<Answer> findById(Long id) throws SQLException;
}
