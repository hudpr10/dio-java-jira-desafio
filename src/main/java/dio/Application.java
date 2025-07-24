package dio;

import dio.persistence.config.ConnectionConfig;
import dio.persistence.migration.MigrationStrategy;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {

	public static void main(String[] args) {
		try {
			Connection connection = ConnectionConfig.getConnection();
			new MigrationStrategy(connection).executeMigration();
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

}
