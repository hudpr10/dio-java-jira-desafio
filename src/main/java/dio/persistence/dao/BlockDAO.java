package dio.persistence.dao;

import dio.persistence.converter.OffsetDateTimeConverter;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;

@AllArgsConstructor
public class BlockDAO {

    private final Connection connection;

    public void blockCard(final long cardId, final String blockReason) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO cards_blocks(blocked_at, blocked_reason, card_id) VALUES (?, ?, ?);");
        statement.setTimestamp(1, OffsetDateTimeConverter.toTimestamp(OffsetDateTime.now()));
        statement.setString(2, blockReason);
        statement.setLong(3, cardId);
        statement.executeUpdate();
    }

}
