package zunpiau.sqljudger.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"teacher_number", "clazz_id"}))
public class Teaching implements Serializable {

    private static final long serialVersionUID = 8572262902500213124L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(nullable = false, name = "teacher_number")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Teacher teacher;
    @JoinColumn(nullable = false, name = "clazz_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Clazz clazz;

    public Teaching(Teacher teacher, Clazz clazz) {
        this.teacher = teacher;
        this.clazz = clazz;
    }
}
