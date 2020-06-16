package rage.sqltrainer.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@Entity
public class Exercise extends AbstractPersistable<Long> {

    @Length(min = 8, max = 64)
    String name;

    @Length(min = 16, max = 1024)
    String handout;

    LocalDateTime created = LocalDateTime.now();

    @ManyToOne
    Database database;
    transient Long databaseId;

    @ManyToOne
    Topic topic;
    transient Long topicId;

    @ManyToOne
    Student student;
            
    String sqlModelSolution;
    
    Boolean disabled = Boolean.FALSE;

}
