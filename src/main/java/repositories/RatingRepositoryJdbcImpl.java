package repositories;

import models.Rating;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RatingRepositoryJdbcImpl implements RatingRepository{

    private DataSource dataSource;
    private static final String SQL_SELECT_FROM_RATING = "SELECT * FROM rating";

    public RatingRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Rating> findById(Long id) throws SQLException {
        String query = SQL_SELECT_FROM_RATING + " WHERE rating_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Rating rating = new Rating(
                            resultSet.getLong("rating_id"),
                            resultSet.getLong("answer_id"),
                            resultSet.getLong("user_id"),
                            resultSet.getInt("value"));
                    return Optional.of(rating);
                }
            }
        }

        return Optional.empty();
    }

    public void executeUpdate(String query, Object... params) throws SQLException {
        if (query == null || query.isEmpty() || params == null || params.length == 0) throw new SQLException("Query or params should not be null or empty");
        try (Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query)){
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
    public Integer getAnswerRating(Long user_id, Long answer_id) throws SQLException {
        String query = SQL_SELECT_FROM_RATING + " WHERE user_id = ? AND answer_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, user_id);
            statement.setLong(2, answer_id);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return resultSet.getInt("value");
                }
            }
        }
        return null;
    }

    @Override
    public Rating findByUserAndAnswer(Long user_id, Long answer_id) throws SQLException {
        String query = SQL_SELECT_FROM_RATING + " WHERE user_id = ? AND answer_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setLong(1, user_id);
            statement.setLong(2, answer_id);
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return Rating.builder()
                            .rating_id(resultSet.getLong("rating_id"))
                            .answer_id(resultSet.getLong("answer_id"))
                            .user_id(resultSet.getLong("user_id"))
                            .value(resultSet.getInt("value"))
                            .build();
                }
            }
        }
        return null;
    }


    @Override
    public Double averageRating(Long id) throws SQLException {
        String query = "SELECT AVG(value) AS average_rating FROM rating WHERE answer_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Double averageRating = resultSet.getDouble("average_rating");
                    return resultSet.wasNull() ? 0.0 : averageRating;
                }
            }
        }
        return 0.0;
    }

    @Override
    public void updateRating(Long answer_id) throws SQLException {
        Double averageRating = averageRating(answer_id);

        String updateQuery = "UPDATE answer SET average_rating = ? WHERE answer_id = ?";
        executeUpdate(updateQuery, averageRating, answer_id);
    }

    @Override
    public void save(Rating entity) throws SQLException {
        String query = "INSERT INTO rating (answer_id, user_id, value) VALUES (?, ?, ?)";
        executeUpdate(query, entity.getAnswer_id(), entity.getUser_id(), entity.getValue());
        updateRating(entity.getAnswer_id());
    }

    @Override
    public void removeById(Long id) throws SQLException {
        String query = "DELETE FROM rating WHERE rating_id = ?";
        executeUpdate(query, id);

        Optional<Rating> rating = findById(id);
        if (rating.isPresent()) {
            averageRating(rating.get().getAnswer_id());
        }
    }
}
