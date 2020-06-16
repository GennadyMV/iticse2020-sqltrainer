package rage.sqltrainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.sqltrainer.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
