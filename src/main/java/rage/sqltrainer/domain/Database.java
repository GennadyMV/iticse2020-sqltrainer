package rage.sqltrainer.domain;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
public class Database extends AbstractPersistable<Long> {

    @Length(min = 6, max = 64)
    String name;

    @Length(min = 0, max = 512)
    String description;

    String sqlInitStatements;

    LocalDateTime created = LocalDateTime.now();

    @ManyToOne
    Student student;

    @OneToMany(mappedBy = "database")
    List<Exercise> exercises;
}
