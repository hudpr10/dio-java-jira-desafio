package dio.service;

import dio.dto.CardDetailsDTO;
import dio.persistence.dao.CardDAO;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

@AllArgsConstructor
public class CardQueryService {

    private final Connection connection;

    public Optional<CardDetailsDTO> findById(final long cardId, final long boardId) throws SQLException {
        CardDAO cardDAO = new CardDAO(connection);
        return cardDAO.findById(cardId, boardId);
    }

}
