package dio.persistence.dao;

import dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@AllArgsConstructor
public class BoardDAO {

    private final Connection connection;

    public BoardEntity insert(final BoardEntity boardEntity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO boards(name) VALUES (?);", Statement.RETURN_GENERATED_KEYS);
        statement.setString(1, boardEntity.getName());
        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()) {
            boardEntity.setId(resultSet.getLong(1));
        }

        return boardEntity;
    }

    public void delete(final long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("DELETE FROM boards WHERE id = ?;");
        statement.setLong(1, id);
        statement.executeUpdate();
    }

    public Optional<BoardEntity> findById(final long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM boards WHERE id = ?;");
        statement.setLong(1, id);
        statement.executeQuery();

        ResultSet resultSet = statement.getResultSet();
        if(resultSet.next()) {
            BoardEntity boardEntity = new BoardEntity();
            boardEntity.setId(resultSet.getLong("id"));
            boardEntity.setName(resultSet.getString("name"));

            return Optional.of(boardEntity);
        } else {
            return Optional.empty();
        }

    }

    public boolean exists(final long id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM boards WHERE id = ?;");
        statement.setLong(1, id);statement.executeQuery();

        ResultSet resultSet = statement.getResultSet();
        return resultSet.next();
    }

}
