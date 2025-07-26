package dio.service;

import dio.dto.BoardColumnIdOrderKindDTO;
import dio.dto.CardDetailsDTO;
import dio.exception.CardBlockedException;
import dio.exception.CardFinishedException;
import dio.exception.EntityNotFoundException;
import dio.persistence.dao.BlockDAO;
import dio.persistence.dao.CardDAO;
import dio.persistence.entity.BoardColumnKindEnum;
import dio.persistence.entity.CardEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    public void moveToNextColumn(final long boardId, final long cardId, List<BoardColumnIdOrderKindDTO> boardColumnIdOrderKindList) throws SQLException {
        try {
            CardDAO cardDAO = new CardDAO(connection);
            Optional<CardDetailsDTO> optional = cardDAO.findById(cardId, boardId);

            // Tratando possíveis erros -----
            CardDetailsDTO cardDetails = optional
                    .orElseThrow(() -> new EntityNotFoundException("O card de ID %s não foi encontrado."
                            .formatted(cardId))
                    );

            if(cardDetails.blocked()) {
                throw new IllegalStateException("O card de ID %s está bloqueado, não é possível mover.".formatted(cardId));
            }

            BoardColumnIdOrderKindDTO currentColumn = boardColumnIdOrderKindList
                    .stream()
                    .filter(column -> column.id() == cardDetails.columnId())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro Board."));

            if(currentColumn.kind().equals(BoardColumnKindEnum.FINAL)) {
                throw new CardFinishedException("O card informado já está na coluna final.");
            }

            BoardColumnIdOrderKindDTO nextColumn = boardColumnIdOrderKindList
                    .stream()
                    .filter(column -> column.columnOrder() == currentColumn.columnOrder() + 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card está cancelado, não é possível mover."));
            // Tratando possíveis erros -----

            cardDAO.moveToColumn(nextColumn.id(), cardId);
            connection.commit();
        } catch(SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    public void cancel(final long boardId, final long cardId, List<BoardColumnIdOrderKindDTO> boardColumnIdOrderKindList) throws SQLException {
        try {
            CardDAO cardDAO = new CardDAO(connection);
            Optional<CardDetailsDTO> optional = cardDAO.findById(cardId, boardId);

            // Tratando possíveis erros -----
            CardDetailsDTO cardDetails = optional
                    .orElseThrow(() -> new EntityNotFoundException("O card de ID %s não foi encontrado."
                            .formatted(cardId))
                    );

            BoardColumnIdOrderKindDTO currentColumn = boardColumnIdOrderKindList
                    .stream()
                    .filter(column -> column.id() == cardDetails.columnId())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro Board."));

            if(currentColumn.kind() == BoardColumnKindEnum.FINAL) {
                throw new IllegalStateException("O card está finalizado, não é possível mover.");
            } else if(currentColumn.kind() == BoardColumnKindEnum.CANCELED) {
                throw new IllegalStateException("O card está cancelado.");
            }

            BoardColumnIdOrderKindDTO cancelColumn = boardColumnIdOrderKindList
                    .stream()
                    .filter(column -> column.kind() == BoardColumnKindEnum.CANCELED)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Testando......"));
            // Tratando possíveis erros -----

            cardDAO.moveToColumn(cancelColumn.id(), cardId);
            connection.commit();
            System.out.println("O card foi cancelado.");
        } catch(SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    public void block(final long boardId, final long cardId, final String blockReason, List<BoardColumnIdOrderKindDTO> boardColumnIdOrderKindList) throws SQLException {
        try {
            CardDAO cardDAO = new CardDAO(connection);
            Optional<CardDetailsDTO> optional = cardDAO.findById(cardId, boardId);

            CardDetailsDTO cardDetails = optional
                    .orElseThrow(() -> new EntityNotFoundException("O card de ID %s não foi encontrado."
                            .formatted(cardId))
                    );

            if(cardDetails.blocked()) {
                throw new IllegalStateException("O card já está bloqueado.");
            }

            BoardColumnIdOrderKindDTO currentColumn = boardColumnIdOrderKindList
                    .stream()
                    .filter(column -> column.id() == cardDetails.columnId())
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("O card informado pertence a outro Board."));

            if(currentColumn.kind() == BoardColumnKindEnum.FINAL || currentColumn.kind() == BoardColumnKindEnum.CANCELED) {
                throw new IllegalStateException("Não é possível bloquear um card na coluna de %s".formatted(currentColumn.kind()));
            }

            BlockDAO blockDAO = new BlockDAO(connection);
            blockDAO.blockCard(cardId, blockReason);
            connection.commit();
            System.out.println("Card bloqueado.");
        } catch(SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }

    public void unblock(final long boardId, final long cardId, final String unblockReason) throws SQLException {
        try {
            CardDAO cardDAO = new CardDAO(connection);
            Optional<CardDetailsDTO> optional = cardDAO.findById(cardId, boardId);
            CardDetailsDTO cardDetails = optional
                    .orElseThrow(() -> new EntityNotFoundException("O card de ID %s não foi encontrado."
                            .formatted(cardId))
                    );

            if(!cardDetails.blocked()) {
                throw new CardBlockedException("O card não está bloqueado.");
            }

            BlockDAO blockDAO = new BlockDAO(connection);
            blockDAO.unblockCard(cardId, unblockReason);
            connection.commit();
            System.out.println("Card desbloqueado.");
        } catch(SQLException ex) {
            connection.rollback();
            throw ex;
        }
    }
}
