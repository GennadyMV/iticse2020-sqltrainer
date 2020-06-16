package rage.sqltrainer.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rage.sqltrainer.domain.Exercise;
import rage.sqltrainer.domain.ExerciseAttempt;
import rage.sqltrainer.domain.Student;

public interface ExerciseAttemptRepository extends JpaRepository<ExerciseAttempt, Long> {
    List<ExerciseAttempt> findByExercise(Exercise e);
    List<ExerciseAttempt> findByStudent(Student s);
    List<ExerciseAttempt> findByExerciseAndStudent(Exercise e, Student s);
    List<ExerciseAttempt> findByStudentAndCorrect(Student s, Boolean correct);

}
