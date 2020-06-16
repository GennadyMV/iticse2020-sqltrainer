package rage.sqltrainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.sqltrainer.domain.Database;

public interface DatabaseRepository extends JpaRepository<Database, Long> {

}
