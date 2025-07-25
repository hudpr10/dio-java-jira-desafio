package dio.service;

import dio.persistence.dao.BoardDAO;
import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class BoardService {

    // Constructor do Lombok
    private final Connection connection;

    public boolean delete(final long id) throws SQLException {
        BoardDAO boardDAO = new BoardDAO(connection);
        boolean exists = false;

        try {
            if(boardDAO.exists(id)) {
                boardDAO.delete(id);
                connection.commit();
                exists = true;
            }
        } catch(SQLException ex) {
            connection.rollback();
            ex.printStackTrace();
        }

        return exists;
    }

}
