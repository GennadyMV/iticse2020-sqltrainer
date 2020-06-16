package rage.sqltrainer.databasecomparison;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import schemacrawler.schema.Table;

public class TableContentComparer {

    public static List<String> compareQueryContent(String expectedQuery, String actualQuery, Connection expectedDb, Connection actualDb) {
        List<String> comparisonResponse = new ArrayList<>();

        List<String> expectedColumnLabels = getColumnLabelsFromQuery(expectedQuery, expectedDb);
        List<String> actualColumnLabels = getColumnLabelsFromQuery(actualQuery, actualDb);
        if (expectedColumnLabels.size() != actualColumnLabels.size()) {
            comparisonResponse.add("Expected that the query result would have " + expectedColumnLabels.size() + " column" + StringUtils.sSuffixFromCount(expectedColumnLabels.size()) + ".");
        }

        for (String expectedColumnLabel : expectedColumnLabels) {
            if (actualColumnLabels.contains(expectedColumnLabel)) {
                continue;
            }

            comparisonResponse.add("Expected that the query would produce a column called " + expectedColumnLabel + ".");
        }

        if (!comparisonResponse.isEmpty()) {
            return comparisonResponse;
        }

        List<Map<String, Object>> expectedContents = getQueryResponseData(expectedQuery, expectedDb);
        List<Map<String, Object>> actualContents = getQueryResponseData(actualQuery, actualDb);

        if (expectedContents.size() != actualContents.size()) {
            comparisonResponse.add("Expected that the query response would have " + expectedContents.size() + " row" + StringUtils.sSuffixFromCount(expectedContents.size()) + ".");
        }

        if (!comparisonResponse.isEmpty()) {
            return comparisonResponse;
        }

//        Set<String> values = expectedContents.stream().flatMap(e -> e.values().stream()).filter(o -> o != null).map(o -> o.toString()).collect(Collectors.toSet());
//        Set<String> parts = Arrays.stream(actualQuery.toLowerCase().split("[,\\s+=]")).filter(p -> p != null).collect(Collectors.toSet());

//        for (String value : values) {
//            if (parts.contains(value.toLowerCase())) {
//                comparisonResponse.add("Did not expect that the query string would include the value " + value + ".");
//            }
//        }

        if (!comparisonResponse.isEmpty()) {
            return comparisonResponse;
        }

        if (expectedContents.equals(actualContents)) {
            return comparisonResponse;
        }

        for (Map<String, Object> expectedContent : expectedContents) {
            boolean found = false;
            for (Map<String, Object> actualContent : actualContents) {
                if (expectedContent.equals(actualContent)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                continue;
            }
            String s = "Expected that the query would return a row with: ";

            List<String> expected = new ArrayList<>();
            for (String expectedColumnLabel : expectedColumnLabels) {
                expected.add(expectedColumnLabel + ": " + expectedContent.get(expectedColumnLabel));
            }

            s += expected.toString() + ".";

            comparisonResponse.add(s);
            break;
        }

        return comparisonResponse;
    }

    public static List<String> compareTableContent(String tableName, Connection expectedConn, Connection actualConn) {
        List<String> comparisonResponse = new ArrayList<>();

        List<String> expectedColumnLabels = getColumnLabelsFromTable(tableName, expectedConn);
        List<String> actualColumnLabels = getColumnLabelsFromTable(tableName, actualConn);
        if (expectedColumnLabels.size() != actualColumnLabels.size()) {
            comparisonResponse.add("Expected that the table " + tableName + " would have " + expectedColumnLabels.size() + " column" + StringUtils.sSuffixFromCount(expectedColumnLabels.size()) + ".");
        }

        for (String expectedColumnLabel : expectedColumnLabels) {
            if (actualColumnLabels.contains(expectedColumnLabel)) {
                continue;
            }

            comparisonResponse.add("Expected that the table " + tableName + " would have a column called " + expectedColumnLabel + ".");
        }
        for (String actualColumnLabel : actualColumnLabels) {
            if (expectedColumnLabels.contains(actualColumnLabel)) {
                continue;
            }

            comparisonResponse.add("Expected that the table " + tableName + " would not have a column called " + actualColumnLabel + ".");
        }

        if (!comparisonResponse.isEmpty()) {
            return comparisonResponse;
        }

        String query = "SELECT * FROM " + tableName;
        
        List<Map<String, Object>> expectedContents = getQueryResponseData(query, expectedConn);
        List<Map<String, Object>> actualContents = getQueryResponseData(query, actualConn);

        if (expectedContents.size() != actualContents.size()) {
            comparisonResponse.add("Expected that the table " + tableName + " would have " + expectedContents.size() + " row" + StringUtils.sSuffixFromCount(expectedContents.size()) + ". Now there was " + actualContents.size() + " row" + StringUtils.sSuffixFromCount(actualContents.size()) + ".");
        }

        if (!comparisonResponse.isEmpty()) {
            return comparisonResponse;
        }

        if (expectedContents.equals(actualContents)) {
            return comparisonResponse;
        }

        for (Map<String, Object> expectedContent : expectedContents) {
            boolean found = false;
            for (Map<String, Object> actualContent : actualContents) {
                if (expectedContent.equals(actualContent)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                continue;
            }

            String s = "Expected that the table " + tableName + " would have a row with: ";

            List<String> expected = new ArrayList<>();
            for (String expectedColumnLabel : expectedColumnLabels) {
                expected.add(expectedColumnLabel + ": " + expectedContent.get(expectedColumnLabel));
            }

            s += expected.toString();
            comparisonResponse.add(s);

            break;
        }

        return comparisonResponse;
    }

    public static List<Map<String, Object>> getTableData(Table table, Connection conn) {
        return getQueryResponseData("SELECT * FROM " + table.getName(), conn);
    }

    public static List<Map<String, Object>> getQueryResponseData(String query, Connection conn) {
        if (query.split(";").length > 1) {
            List<String> queries = new ArrayList<>();
            for (String part : query.split(";")) {
                if (part.trim().isEmpty()) {
                    continue;
                }
                queries.add(part);
            }

            query = queries.get(queries.size() - 1);
        }

        if (!query.trim().toLowerCase().startsWith("select")) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> tableData = new ArrayList<>();

        new JdbcTemplate(new SingleConnectionDataSource(conn, true)).query(query, (rs) -> {
            Map<String, Object> rowData = new LinkedHashMap<>();
            ResultSetMetaData rsmd = rs.getMetaData();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                rowData.put(rsmd.getColumnLabel(i).trim(), rs.getObject(i));
            }

            tableData.add(rowData);
        });

        return tableData;
    }

    public static List<String> getColumnLabelsFromTable(String tableName, Connection conn) {
        List<String> columnLabels = new ArrayList<>();

        try {
            DatabaseMetaData metadata = conn.getMetaData();
            ResultSet resultSet = metadata.getColumns(null, null, tableName, null);

            while (resultSet.next()) {
                columnLabels.add(resultSet.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to extract column labels from table " + tableName + ", error " + e.getMessage());
        }

        return columnLabels;
    }

    public static List<String> getColumnLabelsFromQuery(String query, Connection conn) {
        List<String> columnLabels = new ArrayList<>();

        new JdbcTemplate(new SingleConnectionDataSource(conn, true)).query(query, (rs) -> {
            ResultSetMetaData rsmd = rs.getMetaData();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                String label = rsmd.getColumnLabel(i).trim();
                if (columnLabels.contains(label)) {
                    continue;
                }

                columnLabels.add(label);
            }
        });

        return columnLabels;
    }

}
