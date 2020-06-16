package rage.sqltrainer.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
public class Review extends AbstractPersistable<Long> {

    @ManyToOne
    ExerciseAttempt attempt;

    LocalDateTime created = LocalDateTime.now();

    @ElementCollection
    @CollectionTable(name = "REVIEW_ANSWERS", joinColumns = {
        @JoinColumn(name = "REVIEW_ID")})
    @MapKeyColumn(name = "QUESTION")
    @Column(name = "ANSWER")
    Map<String, String> content = new HashMap<>();
}
