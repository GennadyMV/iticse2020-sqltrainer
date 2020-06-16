package rage.sqltrainer.databasecomparison;

import java.sql.Connection;
import schemacrawler.schema.Catalog;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.schemacrawler.SchemaCrawlerOptionsBuilder;
import schemacrawler.schemacrawler.SchemaInfoLevelBuilder;
import schemacrawler.utility.SchemaCrawlerUtility;

public class CatalogManager {

    public static final Catalog getCatalog(Connection connection) throws SchemaCrawlerException {

        final SchemaCrawlerOptionsBuilder optionsBuilder = SchemaCrawlerOptionsBuilder
                .builder()
                .withSchemaInfoLevel(SchemaInfoLevelBuilder.standard());

        final SchemaCrawlerOptions options = optionsBuilder.toOptions();

        return SchemaCrawlerUtility.getCatalog(connection, options);

    }

}
