package dio.persistence.dao;

import dio.dto.CardDetailsDTO;
import dio.persistence.converter.OffsetDateTimeConverter;
import dio.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import static java.util.Objects.nonNull;

@AllArgsConstructor
public class CardDAO {

    private Connection connection;

    public CardEntity insert(final CardEntity cardEntity) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO cards(title, description, board_column_id) VALUES (?, ?, ?);", Statement.RETURN_GENERATED_KEYS
        );
        statement.setString(1, cardEntity.getTitle());
        statement.setString(2, cardEntity.getDescription());
        statement.setLong(3, cardEntity.getBoardColumn().getId());
        statement.executeUpdate();

        ResultSet resultSet = statement.getGeneratedKeys();
        if(resultSet.next()) {
            cardEntity.setId(resultSet.getLong(1));
        }

        return cardEntity;
    }

    public Optional<CardDetailsDTO> findById(final long cardId, final long boardId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(
                "SELECT " +
                        "    cards.id, " +
                        "    cards.title, " +
                        "    cards.description, " +
                        "    blocks.blocked_at, " +
                        "    blocks.blocked_reason, " +
                        "    cards.board_column_id, " +
                        "    columns.name, " +
                        "    (SELECT COUNT(sub_blocks.id) " +
                        "     FROM cards_blocks sub_blocks " +
                        "     WHERE sub_blocks.card_id = cards.id) AS blocks_amount " +
                        "FROM cards " +
                        "LEFT JOIN cards_blocks blocks " +
                        "    ON cards.id = blocks.card_id AND blocks.unblocked_at IS NULL " +
                        "INNER JOIN boards_columns columns " +
                        "    ON columns.id = cards.board_column_id " +
                        "INNER JOIN boards ON boards.id = columns.board_id " +
                        "WHERE cards.id = ? AND boards.id = ?"
        );
        statement.setLong(1, cardId);
        statement.setLong(2, boardId);
        statement.executeQuery();

        ResultSet resultSet = statement.getResultSet();
        if(resultSet.next()) {
            CardDetailsDTO cardDetails = new CardDetailsDTO(
                    resultSet.getLong("cards.id"),
                    resultSet.getString("cards.title"),
                    resultSet.getString("cards.description"),
                    nonNull(resultSet.getString("blocks.blocked_reason")),
                    OffsetDateTimeConverter.toOffsetDateTime(resultSet.getTimestamp("blocks.blocked_at")),
                    resultSet.getString("blocks.blocked_reason"),
                    resultSet.getInt("blocks_amount"),
                    resultSet.getLong("cards.board_column_id"),
                    resultSet.getString("columns.name")
            );

            return Optional.of(cardDetails);
        }

        return Optional.empty();
    }

    public void moveToColumn(final long columnId, final long cardId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("UPDATE cards SET board_column_id = ? WHERE id = ?;");
        statement.setLong(1, columnId);
        statement.setLong(2, cardId);
        statement.executeUpdate();
    }

}
