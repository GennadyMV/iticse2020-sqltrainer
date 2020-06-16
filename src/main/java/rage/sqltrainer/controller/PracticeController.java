package rage.sqltrainer.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import rage.sqltrainer.databasecomparison.DatabaseComparer;
import rage.sqltrainer.databasecomparison.SQLTrainerConnectionManager;
import rage.sqltrainer.databasecomparison.SchemaPrinter;
import rage.sqltrainer.databasecomparison.TableContentComparer;
import rage.sqltrainer.databasecomparison.TableVo;
import rage.sqltrainer.domain.Database;
import rage.sqltrainer.domain.Exercise;
import rage.sqltrainer.domain.ExerciseAttempt;
import rage.sqltrainer.domain.Student;
import rage.sqltrainer.domain.Topic;
import rage.sqltrainer.repository.ExerciseAttemptRepository;

import rage.sqltrainer.repository.ExerciseRepository;
import rage.sqltrainer.repository.TopicRepository;
import rage.sqltrainer.service.StudentService;
import rage.sqltrainer.service.RewardWeirdBehavior;
import schemacrawler.schema.Table;

import schemacrawler.schemacrawler.SchemaCrawlerException;

@Controller
@Secured("ROLE_USER")
public class PracticeController {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExerciseAttemptRepository exerciseAttemptRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private StudentService studentService;

    @GetMapping("/practice")
    public String viewPracticeIndex(Model model) {
        Student s = studentService.getLoggedInStudent();

        List<Topic> topics = topicRepository.findAll(Sort.by("rank", "name"));
        model.addAttribute("topics", topics);
        Map<Long, Long> completedExercisesPerTopic = new HashMap<>();
        List<ExerciseAttempt> completedAttempts = exerciseAttemptRepository.findByStudentAndCorrect(s, Boolean.TRUE);
        for (Topic topic : topics) {
            Set<Long> completedInTopic = new HashSet<>();
            for (ExerciseAttempt completedAttempt : completedAttempts) {
                if (completedAttempt.getExercise().getTopic().getId().equals(topic.getId())) {
                    completedInTopic.add(completedAttempt.getExercise().getId());
                }
            }
            completedExercisesPerTopic.put(topic.getId(), new Long(completedInTopic.size()));
        }

        Map<Long, Long> createdExercisesPerTopic = new HashMap<>();

        List<Exercise> createdExercises = exerciseRepository.findByStudent(s);

        for (Topic t : topics) {
            createdExercisesPerTopic.put(t.getId(), createdExercises.stream().filter(e -> e.getTopic().getId().equals(t.getId())).count());
        }

        model.addAttribute("created", createdExercisesPerTopic);
        model.addAttribute("completed", completedExercisesPerTopic);

        return "practice/index";
    }

    @GetMapping("/practice/topics/{topicId}/exercises/next")
    public String nextPracticeTask(Model model, @PathVariable Long topicId) {
        Student s = studentService.getLoggedInStudent();

        List<Exercise> exercises = exerciseRepository.findByDisabledIsFalseAndTopic(topicRepository.getOne(topicId));
        List<Exercise> exercisesToChooseFrom = new ArrayList<>(exercises);
        List<ExerciseAttempt> completedAttempts = exerciseAttemptRepository.findByStudentAndCorrect(s, Boolean.TRUE);

        for (ExerciseAttempt completedAttempt : completedAttempts) {
            for (Exercise exercise : exercises) {
                if (exercise.getId().equals(completedAttempt.getExercise().getId())) {
                    exercisesToChooseFrom.remove(exercise);
                }
            }
        }

        if (exercisesToChooseFrom.isEmpty()) {
            model.addAttribute("message", "There are no more exercises in that topic for now..");
            return viewPracticeIndex(model);
        }

        Collections.shuffle(exercisesToChooseFrom);

        return "redirect:/practice/topics/" + topicId + "/exercises/" + exercisesToChooseFrom.get(0).getId();
    }

    @GetMapping("/practice/topics/{topicId}/exercises/{exerciseId}")
    public String viewPracticeExercise(Model model, @PathVariable Long topicId, @PathVariable Long exerciseId) {
        Exercise exercise = exerciseRepository.getOne(exerciseId);
        model.addAttribute("exercise", exercise);
        model.addAttribute("attempts", exerciseAttemptRepository.findByExerciseAndStudent(exercise, studentService.getLoggedInStudent()));

        return "practice/practice";
    }

    @PostMapping("/practice/topics/{topicId}/exercises/{exerciseId}")
    public String submit(Model model, @PathVariable Long topicId, @PathVariable Long exerciseId, String sqlCommands) throws SQLException, SchemaCrawlerException {
        model.addAttribute("sqlCommands", sqlCommands);

        String s = "";
        List<String> queries = new ArrayList<>();
        for (String command : sqlCommands.split(";")) {
            command = command.trim();
            if (command.isEmpty()) {
                continue;
            }
            s += command + ";";
            queries.add(command);
        }

        sqlCommands = s;

        Exercise exercise = exerciseRepository.getOne(exerciseId);
        Database database = exercise.getDatabase();

        ExerciseAttempt exerciseAttempt = new ExerciseAttempt();
        exerciseAttempt.setExercise(exercise);
        exerciseAttempt.setSqlCommands(sqlCommands);
        exerciseAttempt.setStudent(studentService.getLoggedInStudent());

        exerciseAttempt = exerciseAttemptRepository.save(exerciseAttempt);

        String expectedSql = database.getSqlInitStatements() + "\n" + exercise.getSqlModelSolution();
        String actualSql = database.getSqlInitStatements() + "\n" + sqlCommands;

        List<String> errors = new ArrayList<>();
        TableVo table = null;

        try (Connection conn = SQLTrainerConnectionManager.createConnection(actualSql)) {
            table = new TableVo("Query result", TableContentComparer.getQueryResponseData(sqlCommands, conn));
        } catch (Throwable t) {
            errors.add("Error when executing given SQL statements: " + t.getMessage());
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return viewPracticeExercise(model, topicId, exerciseId);
        }

        model.addAttribute("queryResultTable", table);

        try {
            errors = DatabaseComparer.compareDatabases(expectedSql, actualSql);
        } catch (Throwable t) {
            errors.add("Error when executing given SQL statements: " + t.getMessage());
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return viewPracticeExercise(model, topicId, exerciseId);
        }

        exerciseAttempt.setCorrect(Boolean.TRUE);
        exerciseAttempt = exerciseAttemptRepository.save(exerciseAttempt);

        return "redirect:/review/attempts/" + exerciseAttempt.getId();
    }

    @GetMapping("/practice/topics/{topicId}/exercises/{exerciseId}/attempts/{attemptId}")
    public String viewPracticeExerciseAttempt(Model model, @PathVariable Long topicId, @PathVariable Long exerciseId, @PathVariable Long attemptId) {
        ExerciseAttempt attempt = exerciseAttemptRepository.getOne(attemptId);

        if (attempt == null || !attempt.getStudent().getUsername().equals(studentService.getLoggedInStudent().getUsername())) {
            model.addAttribute("sqlCommands", RewardWeirdBehavior.getContent());
            model.addAttribute("fishy", 1);
        } else {
            model.addAttribute("sqlCommands", attempt.getSqlCommands());
        }

        return viewPracticeExercise(model, topicId, exerciseId);
    }

    @GetMapping("/practice/topics/{topicId}/exercises/{exerciseId}/expected")
    public String viewExpectedOutcome(Model model, @PathVariable Long topicId, @PathVariable Long exerciseId) {
        Exercise e = exerciseRepository.getOne(exerciseId);
        model.addAttribute("exercise", e);

        Database database = e.getDatabase();

        try (Connection conn = SQLTrainerConnectionManager.createConnection(database.getSqlInitStatements() + "\n" + e.getSqlModelSolution())) {
            model.addAttribute("queryResultTable", new TableVo("Result table", TableContentComparer.getQueryResponseData(e.getSqlModelSolution(), conn)));
        } catch (Throwable t) {
            // model.addAttribute("schema", "Unable to create schema from given datasource.");
        }

        try (Connection conn = SQLTrainerConnectionManager.createConnection(database.getSqlInitStatements() + "\n" + e.getSqlModelSolution())) {
            List<String> schema = new ArrayList<>();
            List<TableVo> tables = new ArrayList<>();

            for (Table t : DatabaseComparer.getTables(conn)) {
                schema.add(SchemaPrinter.getTableAsString(t));
                tables.add(new TableVo(t.getName(), TableContentComparer.getTableData(t, conn)));
            }

            Collections.sort(schema);
            model.addAttribute("schema", schema);
            model.addAttribute("tables", tables);
        } catch (Throwable t) {
            model.addAttribute("schema", "Unable to create schema from given datasource.");
        }

        return "practice/expected";
    }

}
