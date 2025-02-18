package service;

import dto.TopicDto;
import models.Question;
import models.Topic;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface TopicService {
    Optional<Topic> findById(Long id) throws SQLException;
    Optional<Topic> findByName(String name) throws SQLException;
    List<Topic> findAll() throws SQLException;
    void save(TopicDto topic) throws SQLException;
    void delete(Long id) throws SQLException;
    List<Question> getQuestionsByTopicId(Long topicId) throws SQLException;
}
