package service;

import dto.AnswerDto;

import java.sql.SQLException;
import java.util.List;

public interface AnswerService {
    List<AnswerDto> findByQuestionId(Long id)throws SQLException;
    List<AnswerDto> allAnswers() throws SQLException;
    void saveAnswer(AnswerDto answer) throws SQLException;
    void removeAnswer(Long id) throws SQLException;
}
