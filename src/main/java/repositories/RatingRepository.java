package repositories;

import models.Rating;

import java.sql.SQLException;
import java.util.Optional;

public interface RatingRepository extends CrudRepository<Rating> {
    Integer getAnswerRating(Long user_id, Long answer_id) throws SQLException;
    Double averageRating(Long id) throws SQLException;
    void updateRating(Long answer_id) throws SQLException;
    Optional<Rating> findById(Long id) throws SQLException;
    Rating findByUserAndAnswer(Long user_id, Long answer_id) throws SQLException;
}
