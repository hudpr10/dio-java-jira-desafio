package dio.persistence.dao;

import dio.persistence.entity.BoardColumnEntity;
import dio.persistence.entity.BoardColumnKindEnum;
import lombok.RequiredArgsConstructor;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BoardColumnDAO {

    private final Connection connection;

    public BoardColumnEntity insert(final BoardColumnEntity boardColumnEntity) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO boards_columns(name, column_order, kind, board_id) VALUES (?, ?, ?, ?);");
            int i = 1;
            statement.setString(i++, boardColumnEntity.getName());
            statement.setInt(i++, boardColumnEntity.getOrder());
            statement.setString(i++, boardColumnEntity.getKind().name());
            statement.setLong(i, boardColumnEntity.getBoardEntity().getId());
            statement.executeUpdate();

        } catch(SQLException ex) {
            ex.printStackTrace();
        }

        return boardColumnEntity;
    }

    public List<BoardColumnEntity> findByBoardId(final long id) throws SQLException {
        List<BoardColumnEntity> boardColumnList = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, name, column_order, kind FROM boards_columns WHERE board_id = ? ORDER BY column_order;"
            );
            statement.setLong(1, id);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()) {
                BoardColumnEntity boardColumn = new BoardColumnEntity();
                boardColumn.setId(resultSet.getLong("id"));
                boardColumn.setName(resultSet.getString("name"));
                boardColumn.setOrder(resultSet.getInt("column_order"));
                boardColumn.setKind(BoardColumnKindEnum.findByName(resultSet.getString("kind")));

                boardColumnList.add(boardColumn);
            }
        } catch(SQLException ex) {
            throw ex;
        }

        return boardColumnList;
    }

}
