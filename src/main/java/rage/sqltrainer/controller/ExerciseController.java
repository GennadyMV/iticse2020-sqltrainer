package rage.sqltrainer.controller;

import java.sql.Connection;
import javax.transaction.Transactional;
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
import org.springframework.web.bind.annotation.RequestParam;
import rage.sqltrainer.databasecomparison.SQLTrainerConnectionManager;
import rage.sqltrainer.databasecomparison.TableContentComparer;
import rage.sqltrainer.databasecomparison.TableVo;
import rage.sqltrainer.domain.Database;
import rage.sqltrainer.domain.Exercise;
import rage.sqltrainer.repository.DatabaseRepository;
import rage.sqltrainer.repository.ExerciseRepository;
import rage.sqltrainer.repository.TopicRepository;
import rage.sqltrainer.service.StudentService;

@Controller
public class ExerciseController {

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    DatabaseRepository databaseRepository;

    @Autowired
    TopicRepository topicRepository;

    @Autowired
    StudentService studentService;

    @GetMapping("/exercises")
    @Secured("ROLE_USER")
    String getAll(Model model) {
        model.addAttribute("exercises", exerciseRepository.findAll());
        return "exercises/list";
    }

    @GetMapping("/exercises/add")
    @Secured("ROLE_USER")
    String viewForm(Model model, @ModelAttribute Exercise exercise) {
        model.addAttribute("databases", databaseRepository.findAll());
        model.addAttribute("topics", topicRepository.findAll());
        return "exercises/add";
    }

    @GetMapping("/exercises/add/topics/{topicId}")
    @Secured("ROLE_USER")
    String viewForm(Model model, @ModelAttribute Exercise exercise, @PathVariable Long topicId) {
        model.addAttribute("topicId", topicId);
        return viewForm(model, exercise);
    }

    @GetMapping("/exercises/{id}")
    @Secured("ROLE_ADMIN")
    String getOne(Model model, @PathVariable Long id) {
        model.addAttribute("exercise", exerciseRepository.getOne(id));
        return "exercises/view";
    }

    @PostMapping("/exercises/add/topics/{topicId}")
    @Secured("ROLE_USER")
    @Transactional
    String create(Model model,
            @PathVariable(name = "topicId") Long tId,
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) Long databaseId,
            @RequestParam(required = false, defaultValue = "false") Boolean exerciseVerified,
            @Valid @ModelAttribute Exercise exercise,
            BindingResult res) {
        if (topicId != null) {
            model.addAttribute("topicId", topicId);
        } else {
            model.addAttribute("topicId", tId);
            topicId = tId;
        }

        if (databaseId != null) {
            model.addAttribute("databaseId", databaseId);
        }

        if (databaseId == null) {
            res.addError(new FieldError("exercise", "database", "Please select a database from the list."));
            return viewForm(model, exercise);
        }

        if (topicId == null) {
            res.addError(new FieldError("exercise", "topic", "Please select a topic from the list."));
            return viewForm(model, exercise);
        }

        Database db = databaseRepository.getOne(databaseId);
        String statementsWithDatabase = db.getSqlInitStatements() + "\n" + exercise.getSqlModelSolution();

        try (Connection conn = SQLTrainerConnectionManager.createConnection(statementsWithDatabase)) {
            new TableVo("Query result", TableContentComparer.getQueryResponseData(exercise.getSqlModelSolution(), conn));
        } catch (Throwable throwable) {
            FieldError error = new FieldError("exercise", "sqlModelSolution", exercise.getSqlModelSolution(), false, null, null, "Error in SQL Model Solution (with selected database):\n" + throwable.getMessage());
            res.addError(error);
        }

        if (res.hasErrors()) {
            return viewForm(model, exercise);
        }

        if (!exerciseVerified) {

            TableVo table = null;

            // Checks if the last query -- if it is a sql select query -- makes sense
            try (Connection conn = SQLTrainerConnectionManager.createConnection(db.getSqlInitStatements())) {
                table = new TableVo("Query result", TableContentComparer.getQueryResponseData(exercise.getSqlModelSolution(), conn));
            } catch (Throwable t) {
                FieldError error = new FieldError("exercise", "sqlModelSolution", exercise.getSqlModelSolution(), false, null, null, "Error in SQL Model Solution (with selected database):\n" + t.getMessage());
                res.addError(error);
            }

            if (res.hasErrors()) {
                return viewForm(model, exercise);
            }

            model.addAttribute("queryResultTable", table);

            return viewForm(model, exercise);
        }

        exercise.setDatabase(db);
        exercise.setTopic(topicRepository.getOne(topicId));
        exercise.setStudent(studentService.getLoggedInStudent());

        exerciseRepository.save(exercise);

        return "redirect:/practice";
    }
}
