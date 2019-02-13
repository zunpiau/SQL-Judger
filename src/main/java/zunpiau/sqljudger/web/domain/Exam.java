package zunpiau.sqljudger.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Exam implements Serializable {

    private static final long serialVersionUID = 1115574550960958188L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Clazz clazz;
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Teacher teacher;
    private Long startTime;
    private Long endTime;
    private Boolean cancel = false;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "exam_exercise", joinColumns = @JoinColumn(nullable = false),
            inverseJoinColumns = @JoinColumn(nullable = false))
    private Set<Exercise> exercises;

    public Exam(String title, Clazz clazz, Teacher teacher, Long startTime, Long endTime, Boolean cancel,
            Set<Exercise> exercises) {
        this.title = title;
        this.clazz = clazz;
        this.teacher = teacher;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cancel = cancel;
        this.exercises = exercises;
    }
}
