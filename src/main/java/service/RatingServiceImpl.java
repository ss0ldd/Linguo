package service;

import models.Rating;
import repositories.AnswerRepository;
import repositories.RatingRepository;

import java.sql.SQLException;
import java.util.Optional;

public class RatingServiceImpl implements RatingService{

    private final AnswerRepository answerRepository;
    private final RatingRepository ratingRepository;

    public RatingServiceImpl(AnswerRepository answerRepository, RatingRepository ratingRepository) {
        this.answerRepository = answerRepository;
        this.ratingRepository = ratingRepository;
    }

    @Override
    public boolean hasRating(Long user_id, Long answer_id) throws SQLException {
        return ratingRepository.getAnswerRating(user_id, answer_id) != null;
    }

    @Override
    public void updateOrSaveRating(Rating entity) throws SQLException {
        Integer ratingOptional = ratingRepository.getAnswerRating(entity.getUser_id(), entity.getAnswer_id());
        if (ratingOptional != null){
            System.out.println("I have it");
            Rating rating = ratingRepository.findByUserAndAnswer(entity.getUser_id(), entity.getAnswer_id());
            System.out.println(rating);
            System.out.println(ratingOptional);
            ratingRepository.removeById(rating.getRating_id());
        }
        ratingRepository.save(entity);
    }

    @Override
    public Integer getAnswerRating(Long user_id, Long answer_id) throws SQLException {
        return ratingRepository.getAnswerRating(user_id, answer_id);
    }

    @Override
    public void removeRating(Long rating_id) throws SQLException {
        Optional<Rating> rating = ratingRepository.findById(rating_id);
        if (rating.isPresent()) {
            Long answer_id = rating.get().getAnswer_id();
            ratingRepository.removeById(rating_id);
            ratingRepository.updateRating(answer_id);
        } else {
            throw new IllegalArgumentException("Rating with ID " + rating_id + " not found");
        }

    }

    @Override
    public Double averageRating(Long answer_id) throws SQLException {
        return ratingRepository.averageRating(answer_id);
    }
}
