package dio.persistence.dao;

import dio.persistence.config.ConnectionConfig;
import dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.beans.Statement;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Struct;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.Executor;

@AllArgsConstructor
public class BoardDAO {

    private final Connection connection;

    public BoardEntity insert(final BoardEntity boardEntity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO boards(name) VALUES (?);");
        statement.setString(1, boardEntity.getName());
        statement.executeUpdate();

        ResultSet resultSet = statement.getResultSet();
        if(resultSet.next()) {
            boardEntity.setId(resultSet.getLong("id"));
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
