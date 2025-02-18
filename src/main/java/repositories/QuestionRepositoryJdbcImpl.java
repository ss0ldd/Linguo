package repositories;

import models.Question;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuestionRepositoryJdbcImpl implements QuestionRepository {
    private DataSource dataSource;
    private static final String SQL_SELECT_FROM_QUESTION = "SELECT * FROM question";

    public QuestionRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void executeUpdate(String query, Object... params) throws SQLException {
        if (query == null || query.isEmpty() || params == null || params.length == 0) throw new SQLException("Query or params should not be null or empty");
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof String) {
                    statement.setString(i + 1, (String) params[i]);
                } else if (params[i] instanceof Long) {
                    statement.setLong(i + 1, (Long) params[i]);
                } else {
                    statement.setObject(i + 1, params[i]);
                }
            }
            statement.executeUpdate();
        }
    }

    private Optional<Question> findQuestionByColumn(String query, Object columnValue) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            if (columnValue instanceof String) {
                statement.setString(1, (String) columnValue);
            } else if (columnValue instanceof Long) {
                statement.setLong(1, (Long) columnValue);
            } else {
                statement.setObject(1, columnValue);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToQuestion(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    private Question mapResultSetToQuestion(ResultSet resultSet) throws SQLException {
        return Question.builder()
                .questionId(resultSet.getLong("question_id"))
                .userId(resultSet.getLong("user_id"))
                .text(resultSet.getString("text"))
                .createdAt(resultSet.getDate("created_at"))
                .topic_id(resultSet.getLong("topic_id"))
                .build();
    }

    private List<Question> findQuestionsByQuery(String query, Long params) throws SQLException {
        if (params == null && query.contains("?")) {
            throw new IllegalArgumentException("Parameter cannot be null when the query contains a placeholder");
        }

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (params != null) {
                statement.setLong(1, params); // Установка параметра
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Question> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(mapResultSetToQuestion(resultSet)); // Маппинг результата
                }
                System.out.println("Result: " + result);
                return result;
            }
        }
    }

    @Override
    public List<Question> findAllByUserId(Long userId) throws SQLException {
        String query = SQL_SELECT_FROM_QUESTION + " WHERE user_id = ?";
        System.out.println(userId);
        return findQuestionsByQuery(query, userId);
    }

    @Override
    public Optional<Question> findById(Long id) throws SQLException {
        String query = SQL_SELECT_FROM_QUESTION + " WHERE question_id = ?";
        return findQuestionByColumn(query, id);
    }

    @Override
    public List<Question> findByTopicId(Long topicId) throws SQLException {
        String query = SQL_SELECT_FROM_QUESTION + " WHERE topic_id = ?";
        return findQuestionsByQuery(query, topicId);
    }

    @Override
    public List<Question> findAll() throws SQLException {
        return findQuestionsByQuery(SQL_SELECT_FROM_QUESTION, null);
    }

    @Override
    public Long saveAndReturnId(Question question) throws SQLException {
        String query = "INSERT INTO question(user_id, text, created_at, topic_id) VALUES (?, ?, ?, ?) RETURNING question_id";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, question.getUserId());
            statement.setString(2, question.getText());
            statement.setDate(3, new java.sql.Date(question.getCreatedAt().getTime()));
            statement.setLong(4, question.getTopic_id());

            try (ResultSet generatedKeys = statement.executeQuery()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong("question_id");
                }
            }
        }

        throw new SQLException("Failed to insert question. No ID was generated.");
    }

    @Override
    public void save(Question entity) throws SQLException {
        String query = "INSERT INTO question(user_id, text, created_at, topic_id) VALUES (?, ?, ?, ?)";
        executeUpdate(query, entity.getUserId(), entity.getText(), entity.getCreatedAt(), entity.getTopic_id());
    }

    @Override
    public void removeById(Long id) throws SQLException {
        String query = "DELETE FROM question WHERE question_id = ?";
        executeUpdate(query, id);
    }
}