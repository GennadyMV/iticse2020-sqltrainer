package rage.sqltrainer.databasecomparison;

import java.sql.Connection;
import static rage.sqltrainer.databasecomparison.StringUtils.getNameFromEnd;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import schemacrawler.schema.Catalog;
import schemacrawler.schema.Schema;
import schemacrawler.schemacrawler.SchemaCrawlerException;

public class SchemaPrinter {

    public static String getTableAsString(String tableName, Connection connection) throws SchemaCrawlerException {

        Catalog catalog = CatalogManager.getCatalog(connection);

        for (final Schema schema : catalog.getSchemas()) {
            for (final Table table : catalog.getTables(schema)) {
                String tName = getNameFromEnd(table.getName());
                if (!tName.equalsIgnoreCase(tableName)) {
                    continue;
                }

                return getTableAsString(table);
            }
        }

        return "No such table " + tableName + " found.";
    }

    public static String getTableAsString(Table table) {
        String tableName = getNameFromEnd(table.getName());
        StringBuilder sb = new StringBuilder();

        tableName = tableName.toLowerCase();
        tableName = tableName.substring(0, 1).toUpperCase() + tableName.substring(1);
        sb.append(tableName).append("(");

        for (int i = 0; i < table.getColumns().size(); i++) {
            Column c = table.getColumns().get(i);

            if (c.isPartOfPrimaryKey()) {
                sb.append("(pk) ");
            }

            if (c.isPartOfForeignKey()) {
                sb.append("(fk) ");
            }

            String columnName = getNameFromEnd(c.getShortName());
            sb.append(columnName.trim().toLowerCase());
            sb.append(" ");

            if (c.isPartOfForeignKey()) {
                String referencedTable = c.getReferencedColumn().getShortName();
                String referencedColumn = getNameFromEnd(referencedTable);
                referencedTable = referencedTable.substring(0, referencedTable.lastIndexOf("."));
                referencedTable = referencedTable.replace("\"", "").trim();

                sb.append("-> ").append(referencedTable).append("(").append(referencedColumn).append(")");
            } else {
                sb.append(c.getColumnDataType()).append(c.getWidth());
            }

            if (c.isAutoIncremented()) {
                sb.append(" AUTO_INCREMENT");
            }

            if (c.isPartOfUniqueIndex()) {
                sb.append(" UNIQUE");
            }

            if (c.isPartOfIndex()) {
                sb.append(" INDEX");
            }

            if (i < table.getColumns().size() - 1) {
                sb.append(", ");
            }
        }

        sb.append(")");

        return sb.toString();
    }

}
