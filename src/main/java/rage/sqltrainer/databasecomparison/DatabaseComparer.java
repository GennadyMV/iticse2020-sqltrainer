package rage.sqltrainer.databasecomparison;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;

public class DatabaseComparer {

    public static List<String> compareDatabases(List<String> expectedSql, List<String> actualSql) throws SQLException, SchemaCrawlerException {

        try (Connection expected = SQLTrainerConnectionManager.createConnection(expectedSql);
                Connection actual = SQLTrainerConnectionManager.createConnection(actualSql)) {

            List<Table> expectedTables = getTables(expected);
            List<Table> actualTables = getTables(actual);
    
            // Are the schemas the same?
            List<String> schemaComparisonResult = SchemaComparer.compareDatabaseSchemas(expectedTables, actualTables);
            if (!schemaComparisonResult.isEmpty()) {
                return schemaComparisonResult;
            }

            List<String> tableNames = expectedTables.stream().map(t -> SchemaComparer.getCleanedName(t)).collect(Collectors.toList());

            // Are the data the same?
            for (String tableName : tableNames) {
                schemaComparisonResult = TableContentComparer.compareTableContent(tableName, expected, actual);
                if (!schemaComparisonResult.isEmpty()) {
                    return schemaComparisonResult;
                }
            }

            String lastExpectedQuery = expectedSql.get(expectedSql.size() - 1);
            String lastActualQuery = actualSql.get(actualSql.size() - 1);

            if (!lastExpectedQuery.toLowerCase().trim().startsWith("select")) {
                return schemaComparisonResult;
            }

            // If the last query is a select query, compare their outcomes
            return TableContentComparer.compareQueryContent(lastExpectedQuery, lastActualQuery, expected, actual);
        }

    }

    public static List<String> compareDatabases(String expectedSql, String actualSql) throws SQLException, SchemaCrawlerException {
        return compareDatabases(Arrays.asList(expectedSql.split(";")), Arrays.asList(actualSql.split(";")));
    }

    public static List<Table> getTables(Connection conn) throws SQLException, SchemaCrawlerException {

        final Catalog catalog = CatalogManager.getCatalog(conn);

        return catalog.getSchemas().stream().filter(s -> s.getName().toUpperCase().contains("PUBLIC")).flatMap(s -> catalog.getTables(s).stream()).collect(Collectors.toList());

    }
}
