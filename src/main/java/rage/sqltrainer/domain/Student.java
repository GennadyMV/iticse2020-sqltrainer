package rage.sqltrainer.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
public class Student extends AbstractPersistable<Long> {

    String username;
    LocalDateTime created = LocalDateTime.now();
}
