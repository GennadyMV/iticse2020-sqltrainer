package rage.sqltrainer.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
public class ExerciseAttempt extends AbstractPersistable<Long> {

    @ManyToOne
    Exercise exercise;

    @ManyToOne
    Student student;

    LocalDateTime created = LocalDateTime.now();

    String sqlCommands;

    Boolean correct = false;

}
