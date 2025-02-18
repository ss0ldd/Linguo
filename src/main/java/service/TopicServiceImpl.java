package service;

import dto.TopicDto;
import models.Question;
import models.Topic;
import repositories.QuestionRepository;
import repositories.TopicRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TopicServiceImpl implements TopicService{

    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;

    public TopicServiceImpl(TopicRepository topicRepository, QuestionRepository questionRepository) {
        this.topicRepository = topicRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Topic> findById(Long id) throws SQLException {
        return topicRepository.findById(id);
    }

    @Override
    public Optional<Topic> findByName(String name) throws SQLException {
        return topicRepository.findByName(name);
    }

    @Override
    public List<Topic> findAll() throws SQLException {
        return topicRepository.findAll();
    }

    @Override
    public void save(TopicDto topic) throws SQLException {
        if (topic == null || topic.getTopicName() == null || topic.getTopicName().isEmpty()) {
            throw new IllegalArgumentException("Invalid topic: missing required fields");
        }

        if (findByName(topic.getTopicName()).isPresent()) {
            throw new IllegalArgumentException("Topic with name " + topic.getTopicName() + " already exists");
        }

        Topic topicObj = Topic.builder()
                .topicName(topic.getTopicName())
                .build();

        topicRepository.save(topicObj);
    }

    @Override
    public void delete(Long id) throws SQLException {
        Optional<Topic> topicOptional = findById(id);

        if (topicOptional.isEmpty()) {
            throw new IllegalArgumentException("Topic with ID " + id + " not found");
        }

        List<Question> questions = questionRepository.findByTopicId(id);
        if (!questions.isEmpty()) {
            throw new IllegalStateException("Cannot delete topic with associated questions");
        }

        topicRepository.removeById(id);
    }

    @Override
    public List<Question> getQuestionsByTopicId(Long topicId) throws SQLException {
        return questionRepository.findByTopicId(topicId);
    }
}
