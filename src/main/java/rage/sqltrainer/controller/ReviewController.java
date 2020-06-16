package rage.sqltrainer.controller;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import rage.sqltrainer.databasecomparison.SQLTrainerConnectionManager;
import rage.sqltrainer.databasecomparison.TableContentComparer;
import rage.sqltrainer.databasecomparison.TableVo;
import rage.sqltrainer.domain.ExerciseAttempt;
import rage.sqltrainer.domain.Review;
import rage.sqltrainer.repository.ExerciseAttemptRepository;
import rage.sqltrainer.repository.ReviewRepository;
import rage.sqltrainer.service.StudentService;

@Controller
@Secured("ROLE_USER")
public class ReviewController {

    @Autowired
    ExerciseAttemptRepository exerciseAttemptRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    StudentService studentService;

    @GetMapping("/review/attempts/{attemptId}")
    public String viewExerciseAttemptReviewForm(Model model, @PathVariable Long attemptId) {
        ExerciseAttempt attempt = exerciseAttemptRepository.getOne(attemptId);
        if (attempt == null || !attempt.getStudent().getUsername().equals(studentService.getLoggedInStudent().getUsername())) {
            List<ExerciseAttempt> attempts = exerciseAttemptRepository.findByStudent(studentService.getLoggedInStudent());
            Collections.shuffle(attempts);
            attempt = attempts.get(0);
        }

        model.addAttribute("attempt", attempt);

        Map<String, String> questions = new TreeMap<>();
        // Change review questions here
        questions.put("suitableDifficulty", "The difficulty of the exercise was suitable for me.");
        questions.put("handoutWasClear", "I think that the exercise handout was clear.");
        questions.put("learnedSomething", "I learned something when working on the exercise.");
        questions.put("understandModelSolution", "I understand the expected solution.");
        questions.put("ownSolutionWasGood", "I think that my solution is good.");
        questions.put("workingWasFrustrating", "Working on the exercise was frustrating.");
        questions.put("exerciseTooEasy", "The exercise was too easy.");

        model.addAttribute("questions", questions);

        TableVo table = null;

        // Checks if the last query -- if it is a sql select query -- makes sense
        try (Connection conn = SQLTrainerConnectionManager.createConnection(attempt.getExercise().getDatabase().getSqlInitStatements())) {
            table = new TableVo("Query result", TableContentComparer.getQueryResponseData(attempt.getSqlCommands(), conn));
        } catch (Throwable t) {
        }

        model.addAttribute("queryResultTable", table);

        return "review/form.html";
    }

    @PostMapping("/review/attempts/{attemptId}")
    public String postExerciseAttemptReviewForm(Model model, @RequestParam Map<String, String> reviewResponse, @PathVariable Long attemptId) {
        ExerciseAttempt attempt = exerciseAttemptRepository.getOne(attemptId);
        if (attempt == null || !attempt.getStudent().getUsername().equals(studentService.getLoggedInStudent().getUsername())) {
            return "redirect:/";
        }

        Review r = new Review();
        r.setAttempt(attempt);
        r.setContent(reviewResponse);

        reviewRepository.save(r);

        return "redirect:/practice/topics/" + attempt.getExercise().getTopic().getId() + "/exercises/next";
    }
}
