package rage.sqltrainer.databasecomparison;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import schemacrawler.schemacrawler.SchemaCrawlerException;

public class SchemaComparisonTest {

    private List<String> singleColumnSchema = Arrays.asList(
            "CREATE TABLE Users (name varchar(255));"
    );

    private List<String> singleColumnSchemaWithDifferentTableName = Arrays.asList(
            "CREATE TABLE Kayttajat (name varchar(255));"
    );

    private List<String> singleColumnSchemaWithDifferentAttributeName = Arrays.asList(
            "CREATE TABLE Users (nimi varchar(255));"
    );

    private List<String> singleColumnSchemaWithDifferentParameter = Arrays.asList(
            "CREATE TABLE Users (name varchar(30));"
    );

    private List<String> doubleColumnSchema = Arrays.asList(
            "CREATE TABLE Users (id integer, name varchar(255));"
    );

    private List<String> doubleColumnSchemaDifferentColumnOrder = Arrays.asList(
            "CREATE TABLE Users (name varchar(255), id integer);"
    );

    private List<String> doubleColumnSchemaWithPrimaryKey = Arrays.asList(
            "CREATE TABLE Users (id serial primary key, name varchar(255));"
    );

    private List<String> doubleColumnSchemaWithJustSerial = Arrays.asList(
            "CREATE TABLE Users (id serial, name varchar(255));"
    );

    private List<String> twoTablesdoubleColumnSchema = Arrays.asList(
            "CREATE TABLE Users (id serial primary key, name varchar(255));",
            "CREATE TABLE Jokes (id serial primary key, user_id integer, text varchar(255));"
    );

    private List<String> twoTablesdoubleColumnSchemaWithForeignKey = Arrays.asList(
            "CREATE TABLE Users (id serial primary key, name varchar(255));",
            "CREATE TABLE Jokes (id serial primary key, user_id integer, text varchar(255), FOREIGN KEY(user_id) REFERENCES Users(id));"
    );

    private List<String> twoTablesdoubleColumnSchemaWithSillyForeignKey = Arrays.asList(
            "CREATE TABLE Users (id serial primary key, name varchar(255));",
            "CREATE TABLE Jokes (id serial primary key, user_id integer, text varchar(255), FOREIGN KEY(user_id) REFERENCES Users(name));"
    );

    @Test
    public void singleColumnSchemaIsSame() throws SQLException, SchemaCrawlerException {
        assertTrue(DatabaseComparer.compareDatabases(singleColumnSchema, singleColumnSchema).isEmpty());
    }

    @Test
    public void singleColumnSchemaWithDifferentParameters() throws SQLException, SchemaCrawlerException {
        assertFalse(DatabaseComparer.compareDatabases(singleColumnSchema, singleColumnSchemaWithDifferentParameter).isEmpty());
    }

    @Test
    public void singleColumnSchemaWithDifferentTableName() throws SQLException, SchemaCrawlerException {
        assertFalse(DatabaseComparer.compareDatabases(singleColumnSchema, singleColumnSchemaWithDifferentTableName).isEmpty());
    }

    @Test
    public void singleColumnSchemaWithDifferentAttributeName() throws SQLException, SchemaCrawlerException {
        assertFalse(DatabaseComparer.compareDatabases(singleColumnSchema, singleColumnSchemaWithDifferentAttributeName).isEmpty());
    }

    @Test
    public void differentSchemasAreDifferent() throws SQLException, SchemaCrawlerException {
        assertFalse(DatabaseComparer.compareDatabases(singleColumnSchema, doubleColumnSchema).isEmpty());
    }

    @Test
    public void doubleColumnSchemaIsSame() throws SQLException, SchemaCrawlerException {
        assertTrue(DatabaseComparer.compareDatabases(doubleColumnSchema, doubleColumnSchema).isEmpty());
    }

    @Test
    public void doubleColumnSchemaIsSameEvenIfColumnOrderIsDifferent() throws SQLException, SchemaCrawlerException {
        assertTrue(DatabaseComparer.compareDatabases(doubleColumnSchema, doubleColumnSchemaDifferentColumnOrder).isEmpty());
    }

    @Test
    public void primaryKeysAreCompared() throws SQLException, SchemaCrawlerException {
        assertFalse(DatabaseComparer.compareDatabases(doubleColumnSchema, doubleColumnSchemaWithPrimaryKey).isEmpty());
    }

    @Test
    public void serialAndNotPrimaryKey() throws SQLException, SchemaCrawlerException {
        // apparently data type serial automatically creates -> primary key
        assertTrue(DatabaseComparer.compareDatabases(doubleColumnSchemaWithJustSerial, doubleColumnSchemaWithPrimaryKey).isEmpty());
    }

    @Test
    public void databaseWithDifferentNumberOfTables() throws SQLException, SchemaCrawlerException {
        assertFalse(DatabaseComparer.compareDatabases(doubleColumnSchema, twoTablesdoubleColumnSchema).isEmpty());
    }

    @Test
    public void twoTablesWithAndWithoutForeignKey() throws SQLException, SchemaCrawlerException {
        assertFalse(DatabaseComparer.compareDatabases(twoTablesdoubleColumnSchema, twoTablesdoubleColumnSchemaWithForeignKey).isEmpty());
    }

    @Test
    public void twoTablesWithForeignKeyOtherWithSillyForeignKey() throws SQLException, SchemaCrawlerException {
        assertFalse(DatabaseComparer.compareDatabases(twoTablesdoubleColumnSchema, twoTablesdoubleColumnSchemaWithSillyForeignKey).isEmpty());
    }

}
