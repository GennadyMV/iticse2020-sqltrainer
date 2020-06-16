package rage.sqltrainer.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rage.sqltrainer.domain.Exercise;
import rage.sqltrainer.domain.Student;
import rage.sqltrainer.domain.Topic;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    List<Exercise> findByStudentAndTopic(Student s, Topic t);
    List<Exercise> findByDisabledIsFalseAndTopic(Topic t);
    List<Exercise> findByStudent(Student s);
}
