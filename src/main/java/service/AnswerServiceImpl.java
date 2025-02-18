package service;

import dto.AnswerDto;
import models.Answer;
import repositories.AnswerRepository;
import repositories.RatingRepository;
import repositories.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnswerServiceImpl implements AnswerService{

    UserRepository userRepository;
    AnswerRepository answerRepository;
    RatingRepository ratingRepository;

    public AnswerServiceImpl(UserRepository userRepository, AnswerRepository answerRepository, RatingRepository ratingRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.ratingRepository = ratingRepository;
    }

    public Optional<AnswerDto> answerDtoBuilder(Answer answer){
        try {
            return userRepository.findById(answer.getUserId())
                    .map(user -> AnswerDto.builder()
                            .answerId(answer.getAnswerId())
                            .userId(answer.getUserId())
                            .questionId(answer.getQuestionId())
                            .username(user.getUsername())
                            .text(answer.getText())
                            .createdAt(answer.getCreatedAt())
                            .rating(answer.getRating())
                            .build());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<AnswerDto> returnAnswerDtoList(List<Answer> answers) throws SQLException {
        List<AnswerDto> answerDtos = new ArrayList<>();
        for (Answer answer : answers) {
            Optional<AnswerDto> answerDto = answerDtoBuilder(answer);
            answerDto.ifPresent(answerDtos::add);
        }
        return answerDtos;
    }

    @Override
    public List<AnswerDto> findByQuestionId(Long id) throws SQLException {
        return returnAnswerDtoList(answerRepository.findByQuestionId(id));
    }

    @Override
    public List<AnswerDto> allAnswers() throws SQLException {
        return returnAnswerDtoList(answerRepository.findAll());
    }

    @Override
    public void saveAnswer(AnswerDto answerDto) throws SQLException {
        if ((answerDto == null) || (answerDto.getQuestionId() == null) || (answerDto.getUserId() == null) || (answerDto.getText() == null)) {
            throw new IllegalArgumentException("Invalid AnswerDto: missing required fields");
        }

        Answer answer = Answer.builder()
                .questionId(answerDto.getQuestionId())
                .userId(answerDto.getUserId())
                .text(answerDto.getText())
                .createdAt(answerDto.getCreatedAt())
                .rating(answerDto.getRating())
                .build();
        answerRepository.save(answer);
    }

    @Override
    public void removeAnswer(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("Invalid ProfileDto: missing ID");
        }
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isEmpty()) {
            throw new IllegalArgumentException("Answer with ID " + id + " not found");
        }
        answerRepository.removeById(id);
    }
}
