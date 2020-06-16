package rage.sqltrainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.sqltrainer.domain.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    
}
