package rage.sqltrainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.sqltrainer.domain.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByUsername(String username);
}
