package dio.persistence.dao;

import dio.dto.BoardColumnDTO;
import dio.persistence.entity.BoardColumnEntity;
import dio.persistence.entity.BoardColumnKindEnum;
import dio.persistence.entity.CardEntity;
import lombok.RequiredArgsConstructor;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<BoardColumnEntity> findByBoardId(final long boardId) throws SQLException {
        List<BoardColumnEntity> boardColumnList = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT id, name, column_order, kind FROM boards_columns WHERE board_id = ? ORDER BY column_order;"
            );
            statement.setLong(1, boardId);
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

    public List<BoardColumnDTO> findByBoardIdWithDetails(final long id) throws SQLException {
        List<BoardColumnDTO> boardColumnsDTOs = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT bc.id, bc.name, bc.kind, COUNT(SELECT c.id FROM cards c WHERE c.board_column_id = bc.id) cards_amount " +
                            "FROM boards_columns bc" +
                            "WHERE board_id = ? " +
                            "ORDER BY column_order;"
            );
            statement.setLong(1, id);
            statement.executeQuery();

            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()) {
                BoardColumnDTO boardColumn = new BoardColumnDTO(
                        resultSet.getLong("bc.id"),
                        resultSet.getString("bc.name"),
                        BoardColumnKindEnum.findByName(resultSet.getString("bc.kind")),
                        resultSet.getInt("cards_amount")
                );

                boardColumnsDTOs.add(boardColumn);
            }
        } catch(SQLException ex) {
            throw ex;
        }

        return boardColumnsDTOs;
    }

    public Optional<BoardColumnEntity> findById(final long boardId) throws SQLException {
        Optional<BoardColumnEntity> optional = Optional.empty();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT bc.name, bc.kind, c.id, c.title, c.description " +
                        "FROM boards_columns bc " +
                        "INNER JOIN cards c ON c.board_column_id = bc.id " +
                        "WHERE bc.id = ?;"
        );
        statement.setLong(1, boardId);
        statement.executeQuery();

        ResultSet resultSet = statement.getResultSet();
        if(resultSet.next()) {
            BoardColumnEntity boardColumn = new BoardColumnEntity();
            boardColumn.setName(resultSet.getString("name"));
            boardColumn.setKind(BoardColumnKindEnum.findByName(resultSet.getString("kind")));

            do {
                CardEntity card = new CardEntity();
                card.setId(resultSet.getLong("c.id"));
                card.setTitle(resultSet.getString("c.title"));
                card.setDescription(resultSet.getString("c.description"));
                boardColumn.getCardList().add(card);

            } while(resultSet.next());

            optional = Optional.of(boardColumn);
        }

        return optional;
    }
}
