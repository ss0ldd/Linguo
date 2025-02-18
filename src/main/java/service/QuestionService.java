package service;

import dto.QuestionDto;

import java.sql.SQLException;
import java.util.List;

public interface QuestionService {
    List<QuestionDto> findAllByUserId(Long id) throws SQLException;
    List<QuestionDto> findAll() throws SQLException;
    QuestionDto findById(Long id) throws SQLException;
    List<QuestionDto> findAllByTopicId(Long id) throws SQLException;
    void removeById(Long id) throws SQLException;
    Long saveAndReturnId(QuestionDto questionDto) throws SQLException;
    void save(QuestionDto questionDto) throws SQLException;
}
