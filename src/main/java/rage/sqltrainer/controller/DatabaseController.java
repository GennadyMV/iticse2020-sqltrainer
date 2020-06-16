package rage.sqltrainer.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import rage.sqltrainer.databasecomparison.DatabaseComparer;
import rage.sqltrainer.databasecomparison.SQLTrainerConnectionManager;
import rage.sqltrainer.databasecomparison.SchemaPrinter;
import rage.sqltrainer.databasecomparison.TableContentComparer;
import rage.sqltrainer.databasecomparison.TableVo;
import rage.sqltrainer.domain.Database;
import rage.sqltrainer.repository.DatabaseRepository;
import schemacrawler.schema.Table;

@Controller
@Secured("ROLE_USER")
public class DatabaseController {

    @Autowired
    DatabaseRepository databaseRepository;

    @GetMapping("/databases")
    @Secured("ROLE_ADMIN")
    String getAll(Model model) {
        model.addAttribute("databases", databaseRepository.findAll());
        return "databases/list";
    }

    @GetMapping("/databases/add")
    @Secured("ROLE_ADMIN")
    String viewForm(@ModelAttribute Database database) {
        return "databases/add";
    }

    @GetMapping("/databases/{id}")
    String getOne(Model model, @PathVariable Long id) {
        Database database = databaseRepository.getOne(id);
        model.addAttribute("database", database);

        try (Connection conn = SQLTrainerConnectionManager.createConnection(database.getSqlInitStatements())) {
            List<String> schema = new ArrayList<>();
            List<TableVo> tables = new ArrayList<>();

            for (Table t : DatabaseComparer.getTables(conn)) {
                schema.add(SchemaPrinter.getTableAsString(t));
                tables.add(new TableVo(t.getName(),TableContentComparer.getTableData(t, conn)));
            }

            Collections.sort(schema);
            model.addAttribute("schema", schema);
            model.addAttribute("tables", tables);
        } catch (Throwable t) {
            model.addAttribute("schema", "Unable to create schema from given datasource.");
        }

        return "databases/view";
    }

    @PostMapping("/databases")
    @Secured("ROLE_ADMIN")
    String create(@Valid @ModelAttribute Database database, BindingResult res) {
        if (!database.getSqlInitStatements().trim().endsWith(";") && !database.getSqlInitStatements().trim().isEmpty()) {
            database.setSqlInitStatements(database.getSqlInitStatements().trim() + ";");
        }

        try (Connection conn = SQLTrainerConnectionManager.createConnection(database.getSqlInitStatements())) {

        } catch (Throwable throwable) {
            FieldError error = new FieldError("database", "sqlInitStatements", database.getSqlInitStatements(), false, null, null, "Error in SQL Init statements: " + throwable.getMessage());
            res.addError(error);
        }

        if (res.hasErrors()) {
            return viewForm(database);
        }

        databaseRepository.save(database);
        return "redirect:/databases";
    }
}
