package repositories;

import models.Answer;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnswerRepositoryJdbcImpl implements AnswerRepository {
    private DataSource dataSource;
    private static final String SQL_SELECT_FROM_ANSWER = "SELECT * FROM answer";

    public AnswerRepositoryJdbcImpl(DataSource dataSource) {
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

    private Optional<Answer> findAnswerByColumn(String query, Object columnValue) throws SQLException {
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
                    return Optional.of(mapResultSetToAnswer(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    private Answer mapResultSetToAnswer(ResultSet resultSet) throws SQLException {
        return Answer.builder()
                .answerId(resultSet.getLong("answer_id"))
                .questionId(resultSet.getLong("question_id"))
                .userId(resultSet.getLong("user_id"))
                .text(resultSet.getString("text"))
                .createdAt(resultSet.getDate("created_at"))
                .rating(resultSet.getFloat("average_rating"))
                .build();
    }

    private List<Answer> findAnswersByQuery(String query, Object... params) throws SQLException {
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
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Answer> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(mapResultSetToAnswer(resultSet));
                }
                return result;
            }
        }
    }

    @Override
    public List<Answer> findAll() throws SQLException {
        return findAnswersByQuery(SQL_SELECT_FROM_ANSWER);
    }

    @Override
    public List<Answer> findByQuestionId(Long questionId) throws SQLException {
        String query = SQL_SELECT_FROM_ANSWER + " WHERE question_id = ?";
        return findAnswersByQuery(query, questionId);
    }

    @Override
    public Optional<Answer> findById(Long id) throws SQLException {
        String query = SQL_SELECT_FROM_ANSWER + " WHERE answer_id = ?";
        return findAnswerByColumn(query, id);
    }


    @Override
    public void save(Answer entity) throws SQLException {
        String query = "INSERT INTO answer(question_id, user_id, text, created_at, average_rating) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(query,
                entity.getQuestionId(),
                entity.getUserId(),
                entity.getText(),
                entity.getCreatedAt(),
                entity.getRating());
    }

    @Override
    public void removeById(Long id) throws SQLException {
        String query = "DELETE FROM answer WHERE answer_id = ?";
        executeUpdate(query, id);
    }
}