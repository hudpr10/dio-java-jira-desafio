package dio.service;

import dio.dto.BoardColumnDTO;
import dio.dto.BoardDetailsDTO;
import dio.persistence.dao.BoardColumnDAO;
import dio.persistence.dao.BoardDAO;
import dio.persistence.entity.BoardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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

    public Optional<BoardDetailsDTO> showBoardDetails(final long id) throws SQLException {
        BoardDAO boardDAO = new BoardDAO(connection);
        BoardColumnDAO columnDAO = new BoardColumnDAO(connection);
        Optional<BoardEntity> optionalBoardEntity = boardDAO.findById(id);

        if(optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            List<BoardColumnDTO> columns =  columnDAO.findByBoardIdWithDetails(boardEntity.getId());

            BoardDetailsDTO boardDetailsDTO = new BoardDetailsDTO(boardEntity.getId(), boardEntity.getName(), columns);
            return Optional.of(boardDetailsDTO);
        }

        return Optional.empty();
    }
}
