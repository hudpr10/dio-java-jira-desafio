package dio.service;

import dio.persistence.dao.BoardColumnDAO;
import dio.persistence.entity.BoardColumnEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class BoardColumnQueryService {

    private final Connection connection;

    public Optional<BoardColumnEntity> findById(final long id) throws SQLException {
        BoardColumnDAO boardColumnDAO = new BoardColumnDAO(connection);
        return boardColumnDAO.findById(id);
    }

}
