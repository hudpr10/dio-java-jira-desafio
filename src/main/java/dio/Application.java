package dio;

import dio.persistence.config.ConnectionConfig;
import dio.persistence.migration.MigrationStrategy;
import dio.ui.MainMenu;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {

	public static void main(String[] args) throws SQLException {
		try {
			Connection connection = ConnectionConfig.getConnection();
			new MigrationStrategy(connection).executeMigration();
		} catch(SQLException ex) {
			throw ex;
		}

		new MainMenu().execute();
	}

}
