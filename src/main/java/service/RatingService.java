package service;

import models.Rating;

import java.sql.SQLException;

public interface RatingService {
    boolean hasRating(Long user_id, Long answer_id) throws SQLException;
    void updateOrSaveRating(Rating rating) throws SQLException;
    Integer getAnswerRating(Long user_id, Long answer_id) throws SQLException;
    void removeRating(Long rating_id) throws SQLException;
    Double averageRating(Long answer_id) throws SQLException;

}
