package dio.persistence.dao;

import dio.dto.CardDetailsDTO;
import lombok.AllArgsConstructor;

import java.sql.Connection;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public CardDetailsDTO findById(final long id) {
        return null;
    }

}
