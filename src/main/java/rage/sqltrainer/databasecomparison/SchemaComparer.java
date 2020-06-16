package rage.sqltrainer.databasecomparison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static rage.sqltrainer.databasecomparison.StringUtils.getNameFromEnd;
import static rage.sqltrainer.databasecomparison.StringUtils.sSuffixFromCount;
import schemacrawler.schema.Table;
import schemacrawler.schema.Column;

public class SchemaComparer {

    public static List<String> compareDatabaseSchemas(List<Table> expected, List<Table> actual) {
        Collections.sort(expected, (t1, t2) -> t1.getName().compareTo(t2.getName()));
        Collections.sort(actual, (t1, t2) -> t1.getName().compareTo(t2.getName()));

        List<String> comparisonResult = new ArrayList<>();

        if (expected.size() != actual.size()) {
            comparisonResult.add("Expected that there would be " + expected.size() + " table" + sSuffixFromCount(expected.size()) + ". Now there was " + actual.size() + " table" + sSuffixFromCount(actual.size()) + ".");
        }

        expected.stream().map(t -> getCleanedName(t)).filter(t -> actual.stream().map(a -> getCleanedName(a)).noneMatch(s -> s.equals(t))).forEach(t -> {
            comparisonResult.add("Expected that there would be a table called \"" + t + "\".");
        });

        if (!comparisonResult.isEmpty()) {
            return comparisonResult;
        }
        
        actual.stream().map(t -> getCleanedName(t)).filter(t -> expected.stream().map(a -> getCleanedName(a)).noneMatch(s -> s.equals(t))).forEach(t -> {
            comparisonResult.add("Expected that there would not be a table called \"" + t + "\".");
        });

        if (!comparisonResult.isEmpty()) {
            return comparisonResult;
        }

        for (Table expectedTable : expected) {
            for (Table actualTable : actual) {
                if (!getCleanedName(expectedTable).equals(getCleanedName(actualTable))) {
                    continue;
                }

                comparisonResult.addAll(compareTables(expectedTable, actualTable));
            }

            if (!comparisonResult.isEmpty()) {
                return comparisonResult;
            }
        }

        return comparisonResult;
    }

    public static List<String> compareTables(Table expected, Table actual) {
        String tableName = getCleanedName(expected);
        List<String> comparisonResult = new ArrayList<>();
        if (expected.getColumns().size() != actual.getColumns().size()) {
            comparisonResult.add("Expected that the table " + tableName + " would have " + expected.getColumns().size() + " column" + sSuffixFromCount(expected.getColumns().size()) + ". Now there was " + actual.getColumns().size() + " column" + sSuffixFromCount(actual.getColumns().size()) + ".");
        }

        expected.getColumns().stream().map(c -> getCleanedName(c.getShortName())).filter(c -> actual.getColumns().stream().map(a -> getCleanedName(a.getShortName())).noneMatch(s -> s.equals(c))).forEach(t -> {
            comparisonResult.add("Expected that the table " + tableName + " would have a column called \"" + t + "\".");
        });

        if (!comparisonResult.isEmpty()) {
            return comparisonResult;
        }

        for (int i = 0; i < expected.getColumns().size(); i++) {
            Column expectedColumn = expected.getColumns().get(i);

            for (int j = 0; j < actual.getColumns().size(); j++) {
                Column actualColumn = actual.getColumns().get(j);
                if (!getCleanedName(expectedColumn.getShortName()).equals(getCleanedName(actualColumn.getShortName()))) {
                    continue;
                }

                comparisonResult.addAll(compareColumns(tableName, expectedColumn, actualColumn));
            }
        }

        return comparisonResult;
    }

    private static List<String> compareColumns(String tableName, Column expected, Column actual) {
        List<String> comparisonResult = new ArrayList<>();
        String columnName = getNameFromEnd(expected.getShortName());

        if (!expected.getColumnDataType().equals(actual.getColumnDataType())) {
            comparisonResult.add("Expected that the column " + columnName + " from table " + tableName + " would be of type " + expected.getColumnDataType() + ".");
        }

        if (expected.getWidth() != null && !expected.getWidth().isEmpty() && !expected.getWidth().equals(actual.getWidth())) {
            comparisonResult.add("Expected that the width of the column " + columnName + " in table " + tableName + " would be specified to " + expected.getWidth() + ", i.e. the column should be of type " + expected.getColumnDataType() + "" + expected.getWidth() + ".");
        }

        if (expected.isPartOfPrimaryKey() != actual.isPartOfPrimaryKey()) {
            String error = "The column " + columnName + " in table " + tableName + " should " + (expected.isPartOfPrimaryKey() ? "" : "not") + " be a (part of a) primary key.";
            comparisonResult.add(error);
        }

        if (expected.isPartOfForeignKey() != actual.isPartOfForeignKey()) {
            String error = "The column " + columnName + " in table " + tableName + " should " + (expected.isPartOfForeignKey() ? "" : "not") + " be a (part of a) foreign key.";

            comparisonResult.add(error);
        }

        if (expected.isPartOfForeignKey() && actual.isPartOfForeignKey()) {
            String expectedReferencedTable = expected.getReferencedColumn().getShortName();
            String expectedReferencedColumn = getNameFromEnd(expectedReferencedTable);
            expectedReferencedTable = expectedReferencedTable.substring(0, expectedReferencedTable.lastIndexOf("."));
            expectedReferencedTable = expectedReferencedTable.replace("\"", "").trim();

            String actualReferencedTable = actual.getReferencedColumn().getShortName();
            String actualReferencedColumn = getNameFromEnd(actualReferencedTable);
            actualReferencedTable = actualReferencedTable.substring(0, actualReferencedTable.lastIndexOf("."));
            actualReferencedTable = actualReferencedTable.replace("\"", "").trim();

            if (!expectedReferencedTable.equals(actualReferencedTable) || !expectedReferencedColumn.equals(actualReferencedColumn)) {
                String error = "The column " + columnName + " in table " + tableName + " should be a foreign key that references the column " + expectedReferencedColumn + " in table " + expectedReferencedTable + ".";
                comparisonResult.add(error);
            }
        }

        if (expected.isAutoIncremented() != actual.isAutoIncremented()) {
            String error = "The column " + columnName + " in table " + tableName + " should " + (expected.isAutoIncremented() ? "" : "not") + " be auto incremented.";
            comparisonResult.add(error);
        }

        if (expected.isPartOfUniqueIndex() != actual.isPartOfUniqueIndex()) {
            String error = "The values in column " + columnName + " in table " + tableName + " should " + (expected.isPartOfUniqueIndex() ? "" : "not") + " be unique.";
            comparisonResult.add(error);
        }

        if (expected.isPartOfIndex() != actual.isPartOfIndex()) {
            String error = "The values in column " + columnName + " in table " + tableName + " should " + (expected.isPartOfIndex() ? "" : "not") + " be indexed.";
            comparisonResult.add(error);
        }

        return comparisonResult;
    }

    private static String getCleanedName(String s) {
        return getNameFromEnd(s).toUpperCase().trim();
    }

    public static String getCleanedName(Table expected) {
        return getNameFromEnd(expected.getName()).toUpperCase().trim();
    }
}
