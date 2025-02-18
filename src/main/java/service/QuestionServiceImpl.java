package service;

import dto.QuestionDto;
import models.Question;
import models.Topic;
import models.User;
import repositories.QuestionRepository;
import repositories.TopicRepository;
import repositories.UserRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionServiceImpl implements QuestionService{

    UserRepository userRepository;
    QuestionRepository questionRepository;
    TopicRepository topicRepository;

    public QuestionServiceImpl(UserRepository userRepository, QuestionRepository questionRepository, TopicRepository topicRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
        this.topicRepository = topicRepository;
    }

    public QuestionDto questionDtoBuilder(Question question){
        try {
            System.out.println("Finding user with ID: " + question.getUserId());
            Optional<User> user = userRepository.findById(question.getUserId());
            if (!user.isPresent()) {
                System.err.println("User not found for ID: " + question.getUserId());
                return null;
            }

            System.out.println("Finding topic with ID: " + question.getTopic_id());
            Optional<Topic> topic = topicRepository.findById(question.getTopic_id());
            if (!topic.isPresent()) {
                System.err.println("Topic not found for ID: " + question.getTopic_id());
                return null;
            }

            User userObj = user.get();
            Topic topicObj = topic.get();
            String topicName = topicObj.getTopicName();

            System.out.println(userObj);
            System.out.println(topicObj);
            System.out.println(topicName);
            QuestionDto questionDto = new QuestionDto();
            questionDto.setQuestionId(question.getQuestionId());
            questionDto.setUserId(question.getUserId());
            questionDto.setText(question.getText());
            questionDto.setCreatedAt(question.getCreatedAt());
            questionDto.setUserName(userObj.getUsername());
            questionDto.setTopic(topicName);
            System.out.println(questionDto);
            return questionDto;
        } catch (SQLException e) {
            throw new RuntimeException("Error building QuestionDto", e);
        }

    }

    public List<QuestionDto> returnQuestionDtoList(List<Question> questions) throws SQLException {
        System.out.println("In returnQuestionDtoList" + questions.size());
        List<QuestionDto> questionDtos = new ArrayList<>();
        for (Question question : questions) {
            System.out.println("in returnQuestionDtoList");
            System.out.println(question);
            QuestionDto questionDto = questionDtoBuilder(question);
            System.out.println(questionDto);
            questionDtos.add(questionDto);
            System.out.println(questionDtos);
        }
        return questionDtos;
    }

    @Override
    public List<QuestionDto> findAllByUserId(Long id) throws SQLException {
        List<Question> questions = questionRepository.findAllByUserId(id);
        System.out.println(questions);
        List<QuestionDto> questionDtos = returnQuestionDtoList(questions);
        System.out.println(questionDtos);
        return questionDtos;
    }

    @Override
    public List<QuestionDto> findAllByTopicId(Long id) throws SQLException {
        return returnQuestionDtoList(questionRepository.findByTopicId(id));
    }

    @Override
    public List<QuestionDto> findAll() throws SQLException {
        return returnQuestionDtoList(questionRepository.findAll());
    }

    @Override
    public QuestionDto findById(Long id) throws SQLException {
        Optional<Question> questionOptional = questionRepository.findById(id);
        return questionOptional.map(this::questionDtoBuilder).orElse(null);
    }

    @Override
    public void removeById(Long id) throws SQLException {
        if (id == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        Optional<Question> questionOptional= questionRepository.findById(id);
        if (questionOptional.isEmpty()){
            throw new IllegalArgumentException("Question not found: " + id);
        }
        questionRepository.removeById(id);
    }

    @Override
    public Long saveAndReturnId(QuestionDto questionDto) throws SQLException {
        Question question = Question.builder()
                .userId(questionDto.getUserId())
                .text(questionDto.getText())
                .createdAt(questionDto.getCreatedAt())
                .topic_id(topicRepository.findTopicIdByName(questionDto.getTopic()))
                .build();

        // Сохраняем вопрос и получаем сгенерированный ID
        return questionRepository.saveAndReturnId(question);
    }

    @Override
    public void save(QuestionDto questionDto) throws SQLException {
        if (questionDto == null || questionDto.getText() == null || questionDto.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be null or empty");
        }

        // Проверяем, что topic существует
        Long topicId = topicRepository.findTopicIdByName(questionDto.getTopic());
        if (topicId == null) {
            throw new IllegalArgumentException("Topic not found: " + questionDto.getTopic());
        }

        // Преобразуем QuestionDto в модель Question
        Question question = Question.builder()
                .userId(questionDto.getUserId())
                .text(questionDto.getText())
                .createdAt(questionDto.getCreatedAt() != null ?
                        questionDto.getCreatedAt() :
                        new java.sql.Date(System.currentTimeMillis()))
                .topic_id(topicId)
                .build();

        // Сохраняем вопрос в базу данных
        questionRepository.save(question);
    }
}
