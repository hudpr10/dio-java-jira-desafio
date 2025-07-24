package dio.persistence.migration;

import dio.persistence.config.ConnectionConfig;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.AllArgsConstructor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;

@AllArgsConstructor
public class MigrationStrategy {

    private final Connection connection;

    public void executeMigration() {
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        try {
            FileOutputStream fos = new FileOutputStream("liquibase.log");

            System.setOut(new PrintStream(fos));
            System.setErr(new PrintStream(fos));

            // Configurando o liquibase -------------------------------------------
            try {
                Connection connection = ConnectionConfig.getConnection();
                JdbcConnection jdbcConnection = new JdbcConnection(connection);

                Liquibase liquibase = new Liquibase(
                        "/db/changelog/db.changelog-master.yml",
                        new ClassLoaderResourceAccessor(),
                        jdbcConnection
                );

                liquibase.update();

            } catch(SQLException | LiquibaseException ex) {
                    ex.printStackTrace();
            }
            // Configurando o liquibase -------------------------------------------
            System.setErr(originalErr);

        } catch(IOException ex) {
            ex.printStackTrace();
        }

        System.setOut(originalOut);
        System.setErr(originalErr);
    }

}
