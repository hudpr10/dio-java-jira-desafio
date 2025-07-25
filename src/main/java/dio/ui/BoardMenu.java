package dio.ui;

import dio.dto.BoardDetailsDTO;
import dio.persistence.config.ConnectionConfig;
import dio.persistence.entity.BoardEntity;
import dio.service.BoardQueryService;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

@AllArgsConstructor
public class BoardMenu {

    private final BoardEntity boardEntity;
    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() {
        try {
            System.out.printf("Bem vindo ao BOARD %s", boardEntity.getName());
            System.out.println("Selecione a operacao desejada:");

            int option = -1;
            while(option != 9) {
                System.out.println("1. Criar um card.");
                System.out.println("2. Mover um card.");
                System.out.println("3. Bloquear um card.");
                System.out.println("4. Desbloquar um card.");
                System.out.println("5. Cancelar um card.");

                System.out.println("6. Ver board.");
                System.out.println("7. Ver cards.");
                System.out.println("8. Ver colunas com cards.");

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
                    case 7 -> showCards();
                    case 8 -> showColumnsAndCards();
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

    private void createCard() {
    }

    private void moveCardToNextColumn() {
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
                    System.out.printf("BOARD ID: %s | Nome: %s", b.id(), b.name());
                    b.boardColumnDTOs().forEach(c -> {
                        System.out.printf("\nColuna %s | Tipo: %s | Quantidade de Cards: %s", c.name(), c.kind(), c.cardsAmount());
                    });
                }
        );
    }

    private void showCards() {
    }

    private void showColumnsAndCards() {
    }
}
