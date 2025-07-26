package dio.service;

import dio.persistence.dao.CardDAO;
import dio.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class CardService {

    private final Connection connection;

    public CardEntity insert(final CardEntity cardEntity) throws SQLException {
        try {
            CardDAO cardDAO = new CardDAO(connection);
            cardDAO.insert(cardEntity);
            connection.commit();

            return cardEntity;
        } catch(SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

}
