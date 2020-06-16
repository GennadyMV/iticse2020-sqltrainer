package rage.sqltrainer.databasecomparison;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class SQLTrainerConnectionManager {

    public static Connection createConnection(String sqlStatements) throws SQLException {
        return createConnection(Arrays.asList(sqlStatements.split(";")));
    }

    public static Connection createConnection(List<String> sqlStatements) throws SQLException {
        Connection conn = getConnection();

        JdbcTemplate tmpl = new JdbcTemplate(new SingleConnectionDataSource(conn, false));

        for (String statement : sqlStatements) {
            try {

                if (statement.trim().toLowerCase().startsWith("select")) {
                    tmpl.execute(statement);
                } else {
                    tmpl.update(statement);
                }
                
            } catch (Throwable t) {
                System.out.println("ERROR! " + t.getMessage());
                System.out.println("Error when executing " + statement);
                t.printStackTrace();
                System.out.println("");
            }
        }

        return conn;
    }

    private static Connection getConnection() throws SQLException {
        String connectionName = UUID.randomUUID().toString().substring(0, 6);
        String jdbcUrl = "jdbc:h2:mem:sqltrainer-" + connectionName;

        return DriverManager.getConnection(jdbcUrl, "", "");
    }
}
