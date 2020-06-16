package rage.sqltrainer.api;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import rage.sqltrainer.domain.Exercise;
import rage.sqltrainer.domain.ExerciseAttempt;
import rage.sqltrainer.domain.Student;
import rage.sqltrainer.repository.ExerciseAttemptRepository;
import rage.sqltrainer.repository.ExerciseRepository;
import rage.sqltrainer.security.UserDetailsRetrievingService;
import rage.sqltrainer.service.StudentService;

@RestController
public class PointsApiController {

    @Autowired
    StudentService studentService;

    @Autowired
    ExerciseRepository exerciseRepository;

    @Autowired
    ExerciseAttemptRepository exerciseAttemptRepository;

    @Autowired
    UserDetailsRetrievingService userService;

    @CrossOrigin("*")
    @GetMapping("/api/courses/{course}/users/current/progress")
    public Map<String, Map<String, Set<Long>>> getPointsForUser(@PathVariable String course, @RequestHeader(value = "Authorization", required = false) String accessToken) {
        Student loggedInStudent = studentService.getLoggedInStudent(accessToken);

        Map<String, Set<Long>> exercisesCreatedPerTopic = getExercisesCreatedPerTopic(loggedInStudent);
        Map<String, Set<Long>> exercisesCompletedPerTopic = getExercisesCompletedPerTopic(loggedInStudent);
        
        Map<String, Map<String, Set<Long>>> studentCompletions = new TreeMap<>();
        
        for (Map.Entry<String, Set<Long>> entry : exercisesCreatedPerTopic.entrySet()) {
            studentCompletions.putIfAbsent(entry.getKey(), new TreeMap<>());
            studentCompletions.get(entry.getKey()).put("created", entry.getValue());
        }
        
        for (Map.Entry<String, Set<Long>> entry : exercisesCompletedPerTopic.entrySet()) {
            studentCompletions.putIfAbsent(entry.getKey(), new TreeMap<>());
            studentCompletions.get(entry.getKey()).put("completed", entry.getValue());
        }

        return studentCompletions;
    }

    private Map<String, Set<Long>> getExercisesCreatedPerTopic(Student student) {

        Map<String, Set<Long>> exercisesCreatedPerTopic = new TreeMap<>();

        for (Exercise exercise : exerciseRepository.findByStudent(student)) {
            String topicName = exercise.getTopic().getName();
            exercisesCreatedPerTopic.putIfAbsent(topicName, new TreeSet<>());
            exercisesCreatedPerTopic.get(topicName).add(exercise.getId());
        }

        return exercisesCreatedPerTopic;
    }

    private Map<String, Set<Long>> getExercisesCompletedPerTopic(Student student) {

        Map<String, Set<Long>> exercisesCompletedPerTopic = new TreeMap<>();

        for (ExerciseAttempt exerciseAttempt : exerciseAttemptRepository.findByStudentAndCorrect(student, Boolean.TRUE)) {
            String topicName = exerciseAttempt.getExercise().getTopic().getName();
            exercisesCompletedPerTopic.putIfAbsent(topicName, new TreeSet<>());
            exercisesCompletedPerTopic.get(topicName).add(exerciseAttempt.getExercise().getId());
        }

        return exercisesCompletedPerTopic;

    }
}
