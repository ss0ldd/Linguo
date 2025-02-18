package repositories;

import models.Question;
import models.Topic;
import models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TopicRepositoryJdbcImpl implements TopicRepository {
    private DataSource dataSource;

    private static final String SQL_SELECT_FROM_TOPIC = "SELECT * FROM topic";
    public TopicRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void executeUpdate(String query, Object... params) throws SQLException {
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

    @Override
    public void save(Topic entity) throws SQLException {
        String query = "INSERT INTO topic(name) VALUES (?)";
        executeUpdate(query, entity.getTopicName());
    }

    @Override
    public void removeById(Long id) throws SQLException {
        String query = "DELETE FROM topic WHERE topic_id = ?";
        executeUpdate(query, id);
    }

    @Override
    public Optional<Topic> findByName(String name) throws SQLException {
        String query = SQL_SELECT_FROM_TOPIC + " WHERE name = ?";
        return findTopicByColumn(query, name);
    }

    @Override
    public Optional<Topic> findById(Long id) throws SQLException {
        String query = SQL_SELECT_FROM_TOPIC + " WHERE topic_id = ?";
        return findTopicByColumn(query, id);
    }

    @Override
    public List<Topic> findAll() throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SELECT_FROM_TOPIC)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                List<Topic> result = new ArrayList<>();
                while (resultSet.next()) {
                    result.add(mapResultSetToTopic(resultSet));
                }
                return result;
            }
        }
    }

    private Optional<Topic> findTopicByColumn(String query, Object columnValue) throws SQLException {
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
                    return Optional.of(mapResultSetToTopic(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Long findTopicIdByName(String name) throws SQLException {
        String query = SQL_SELECT_FROM_TOPIC + " WHERE name = ?";
        Optional<Topic> topic = findTopicByColumn(query, name);
        if (topic.isPresent()) {
            return topic.get().getTopicId();
        }
        return null;
    }

    private Topic mapResultSetToTopic(ResultSet resultSet) throws SQLException {
        return Topic.builder()
                .topicId(resultSet.getLong("topic_id"))
                .topicName(resultSet.getString("name"))
                .build();
    }
}
