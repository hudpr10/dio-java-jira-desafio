package dio.ui;

import dio.dto.BoardColumnIdOrderKindDTO;
import dio.dto.BoardDetailsDTO;
import dio.persistence.config.ConnectionConfig;
import dio.persistence.entity.BoardColumnEntity;
import dio.persistence.entity.BoardEntity;
import dio.persistence.entity.CardEntity;
import dio.service.BoardColumnQueryService;
import dio.service.BoardQueryService;
import dio.service.CardQueryService;
import dio.service.CardService;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@AllArgsConstructor
public class BoardMenu {

    private final BoardEntity boardEntity;
    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() {
        try {

            System.out.printf("\nBem vindo ao BOARD %s\n", boardEntity.getName());
            System.out.println("Selecione a operacao desejada:");

            int option = -1;
            while(option != 9) {
                System.out.println("1. Criar um card.");
                System.out.println("2. Mover um card.");
                System.out.println("3. Bloquear um card.");
                System.out.println("4. Desbloquar um card.");
                System.out.println("5. Cancelar um card.");

                System.out.println("6. Ver board.");
                System.out.println("7. Ver colunas com cards.");
                System.out.println("8. Ver card pelo ID.");

                System.out.println("9. Voltar ao menu anterior.");
                System.out.println("10. Sair do sistema.");

                option = scanner.nextInt();
                switch(option) {
                    case 1 -> createCard();
                    case 2 -> moveCardToNextColumn();
                    case 3 -> blockCard();
                    case 4 -> unblockCard();
                    case 5 -> cancelCard();
                    case 6 -> showBoard();
                    case 7 -> showColumns();
                    case 8 -> showCards();
                    case 9 -> System.out.println("Voltando ao menu anterior");
                    case 10 -> System.exit(0);
                    default -> System.out.println("Opção inválida.");
                }
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private void createCard() throws SQLException {
        CardEntity cardEntity = new CardEntity();
        System.out.print("Informe o título do card: ");
        cardEntity.setTitle(scanner.next());

        System.out.print("Informe a descrição do card: ");
        cardEntity.setDescription(scanner.next());

        cardEntity.setBoardColumn(boardEntity.getInitialColumn());

        Connection connection = ConnectionConfig.getConnection();
        new CardService(connection).insert(cardEntity);
    }

    private void moveCardToNextColumn() throws SQLException {
        System.out.print("Informe o ID do card: ");
        long cardId = scanner.nextLong();

        List<BoardColumnIdOrderKindDTO> boardColumnIdOrderKindList = boardEntity
                .getBoardsColumns()
                .stream()
                .map(column -> new BoardColumnIdOrderKindDTO(column.getId(), column.getOrder(), column.getKind()))
                .toList();
        Connection connection = ConnectionConfig.getConnection();

        try {
            new CardService(connection).moveToNextColumn(boardEntity.getId(), cardId, boardColumnIdOrderKindList);
        } catch(RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void blockCard() {
    }

    private void unblockCard() {
    }

    private void cancelCard() {
    }

    private void showBoard() throws SQLException {
        Connection connection = ConnectionConfig.getConnection();
        Optional<BoardDetailsDTO> optional = new BoardQueryService(connection).showBoardDetails(boardEntity.getId());

        optional.ifPresent(
                b -> {
                    System.out.printf("BOARD ID: %s | Nome: %s\n", b.id(), b.name());
                    b.boardColumnDTOs().forEach(c -> {
                        System.out.printf("Coluna %s | Tipo: %s | Quantidade de Cards: %s\n", c.name(), c.kind(), c.cardsAmount());
                    });
                }
        );
    }

    private void showColumns() throws SQLException {
        List<Long> columnsIds = boardEntity.getBoardsColumns().stream().map(BoardColumnEntity::getId).toList();

        long selectedColumn = -1L;
        while(!columnsIds.contains(selectedColumn)) {
            System.out.printf("Escolha uma coluna do Board %s\n", boardEntity.getName());
            boardEntity.getBoardsColumns().forEach(c -> System.out.printf("%s. %s [%s]\n", c.getId(), c.getName(), c.getKind()));
            selectedColumn = scanner.nextLong();
        }

        Connection connection = ConnectionConfig.getConnection();
        Optional<BoardColumnEntity> optional = new BoardColumnQueryService(connection).findById(selectedColumn);
        optional.ifPresent(co -> {
            System.out.printf("Coluna: %s | Tipo: %s\n", co.getName(), co.getKind());
            co.getCardList().forEach(ca ->
                    System.out.printf("Card: %s. %s\nDescrição: %s\n", ca.getId(), ca.getTitle(), ca.getDescription())
            );
        });
    }

    private void showCards() throws SQLException {
        System.out.print("Informe o ID do CARD que deseja visualizar: ");
        long selectedCard = scanner.nextLong();

        Connection connection = ConnectionConfig.getConnection();
        new CardQueryService(connection).findById(selectedCard, boardEntity.getId())
                .ifPresentOrElse(c -> {
                            System.out.printf("Card %s. %s\n", c.id(), c.title());
                            System.out.println("Descrição: " + c.description());
                            if(c.blocked()) {
                                System.out.println("Está bloqueado.");
                                System.out.println("Motivo: " + c.blockedReason());
                            } else {
                                System.out.println("Não está bloqueado.");
                            }
                            System.out.println("Já foi bloqueado " + c.blocksAmount() + " vezes.");
                            System.out.printf("Está no momento na coluna: %s. %s\n", c.columnId(), c.columnName());
                        },
                        () -> System.out.println("Não existe um CARD com o ID informado"));
    }
}
