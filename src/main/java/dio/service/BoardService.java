package dio.service;

import dio.persistence.dao.BoardColumnDAO;
import dio.persistence.dao.BoardDAO;
import dio.persistence.entity.BoardColumnEntity;
import dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
public class BoardService {

    // Constructor do Lombok
    private final Connection connection;

    public BoardEntity insert(final BoardEntity boardEntity) throws SQLException {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO boardColumnDAO = new BoardColumnDAO(connection);

        try {
            boardDAO.insert(boardEntity);

            List<BoardColumnEntity> boardsColumns = boardEntity.getBoardsColumns().stream().map(bc -> {
                bc.setBoardEntity(boardEntity);
                return bc;
            }).toList();
            for(BoardColumnEntity bc : boardsColumns) {
                boardColumnDAO.insert(bc);
            }

            connection.commit();

        } catch(SQLException ex) {
            connection.rollback();
            ex.printStackTrace();
        }

        return boardEntity;
    }

    public boolean delete(final long id) throws SQLException {
        BoardDAO boardDAO = new BoardDAO(connection);
        boolean exists = false;

        try {
            if(boardDAO.exists(id)) {
                boardDAO.delete(id);
                connection.commit();
                exists = true;
            }
        } catch(SQLException ex) {
            connection.rollback();
            ex.printStackTrace();
        }

        return exists;
    }

}
