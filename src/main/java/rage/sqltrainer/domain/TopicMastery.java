package rage.sqltrainer.domain;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;

// WIP: separate branch
@Data
@Entity
public class TopicMastery extends AbstractPersistable<Long> {

    @ManyToOne
    Topic topic;
    @ManyToOne
    Student student;
    
    Double mastery = 0.0;

}
