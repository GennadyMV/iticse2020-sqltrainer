package rage.sqltrainer.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
public class Topic extends AbstractPersistable<Long> {

    @Length(min = 8, max = 64)
    String name;
    @Length(min = 16, max = 512)
    String description;
    Integer rank;

    LocalDateTime created = LocalDateTime.now();

    @OneToMany(mappedBy = "topic")
    List<Exercise> exercises = new ArrayList<>();
}
