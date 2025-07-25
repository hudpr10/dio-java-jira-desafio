package dio.ui;

import dio.persistence.config.ConnectionConfig;
import dio.persistence.entity.BoardColumnEntity;
import dio.persistence.entity.BoardColumnKindEnum;
import dio.persistence.entity.BoardEntity;
import dio.service.BoardQueryService;
import dio.service.BoardService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class MainMenu {

    private final Scanner scanner = new Scanner(System.in).useDelimiter("\n");

    public void execute() throws SQLException {
        System.out.println("Boas vindas ao gerenciador de tarefas!");
        System.out.println("Escolha uma opção: ");

        int option = -1;
        while(true) {
            System.out.println("1. Criar novo Board.");
            System.out.println("2. Selecionar Board.");
            System.out.println("3. Excluir Board.");
            System.out.println("4. Sair.");
            option = scanner.nextInt();
            switch(option) {
                case 1 -> createBoard();
                case 2 -> selectBoard();
                case 3 -> deleteBoard();
                case 4 -> System.exit(0);
                default -> System.out.println("Opção inválida, informe umas das possíveis opções");
            }
        }
    }

    private void createBoard() throws SQLException {
        BoardEntity boardEntity = new BoardEntity();

        System.out.print("Informe o nome do novo BOARD: ");
        boardEntity.setName(scanner.next());

        System.out.println("\nSeu BOARD terá colunas além das 3 padrões? Se sim informe quantas, senão digite '0'.");
        int additionalColumns = scanner.nextInt();

        List<BoardColumnEntity> boardColumnList = new ArrayList<>();

        System.out.print("Informe o nome da coluna inicial do BOARD: ");
        String initialColumnName = scanner.next();
        BoardColumnEntity initialColumn = createColumn(initialColumnName, BoardColumnKindEnum.INITIAL, 0);
        boardColumnList.add(initialColumn);

        for(int i = 0; i < additionalColumns; i++) {
            System.out.print("Informe o nome da coluna de tarefa pendente do BOARD: ");
            String columnName = scanner.next();
            BoardColumnEntity column = createColumn(columnName, BoardColumnKindEnum.PENDING, i+1);
            boardColumnList.add(column);
        }

        System.out.print("Informe o nome da coluna final do BOARD: ");
        String finalColumnName = scanner.next();
        BoardColumnEntity finalColumn = createColumn(finalColumnName, BoardColumnKindEnum.FINAL, additionalColumns+1);
        boardColumnList.add(finalColumn);

        System.out.print("Informe o nome da coluna de cancelados do BOARD: ");
        String canceledColumnName = scanner.next();
        BoardColumnEntity canceledColumn = createColumn(canceledColumnName, BoardColumnKindEnum.CANCELED, additionalColumns+2);
        boardColumnList.add(canceledColumn);

        System.out.println();
        boardEntity.setBoardsColumns(boardColumnList);

        try {
            Connection connection = ConnectionConfig.getConnection();
            BoardService boardService = new BoardService(connection);

            boardService.insert(boardEntity);

        } catch(SQLException ex) {
            throw ex;
        }
    }

    private void selectBoard() throws SQLException {
        System.out.print("Informe o ID do BOARD que deseja selecionar: ");
        long id = scanner.nextLong();

        try {
            Connection connection = ConnectionConfig.getConnection();
            BoardQueryService boardQueryService = new BoardQueryService(connection);

            Optional<BoardEntity> optional = boardQueryService.findById(id);
            optional.ifPresentOrElse(
                    b -> new BoardMenu(b).execute(),
                    () -> System.out.printf("Não foi encontrado um BOARD com ID %s\n", id)
            );

            System.out.println();

        } catch (SQLException ex) {
            throw ex;
        }
    }

    private void deleteBoard() throws SQLException {
        System.out.print("Informe o ID do BOARD que gostaria de remover: ");
        long id = scanner.nextLong();

        try {
            Connection connection = ConnectionConfig.getConnection();
            BoardService boardService = new BoardService(connection);

            if(boardService.delete(id)) {
                System.out.printf("O BOARD %s foi excluido\n", id);
            } else {
                System.out.printf("Não foi encontrado um BOARD com ID %s\n", id);
            }

            System.out.println();

        } catch (SQLException e) {
            throw e;
        }
    }

    private BoardColumnEntity createColumn(final String name, final BoardColumnKindEnum kind, final int columnOrder) {
        BoardColumnEntity boardColumn = new BoardColumnEntity();

        boardColumn.setName(name);
        boardColumn.setKind(kind);
        boardColumn.setOrder(columnOrder);

        return boardColumn;
    }

}
