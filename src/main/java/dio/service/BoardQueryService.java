package dio.service;

import dio.persistence.dao.BoardColumnDAO;
import dio.persistence.dao.BoardDAO;
import dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardQueryService {

    private final Connection connection;

    public Optional<BoardEntity> findById(final long id) throws SQLException {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO columnDAO = new BoardColumnDAO(connection);
        Optional<BoardEntity> optionalBoardEntity = boardDAO.findById(id);

        if(optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            boardEntity.setBoardsColumns(columnDAO.findByBoardId(boardEntity.getId()));
            return Optional.of(boardEntity);
        }

        return Optional.empty();
    }

}
