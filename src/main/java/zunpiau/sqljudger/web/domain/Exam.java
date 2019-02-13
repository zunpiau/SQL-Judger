package zunpiau.sqljudger.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    private Teaching teaching;
    private Long startTime;
    private Long endTime;
    private Boolean cancel = false;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "exam_exercise", joinColumns = @JoinColumn(nullable = false))
    private Set<Long> exercises;

    public Exam(String title, Teaching teaching, Long startTime, Long endTime, Boolean cancel, Set<Long> exercises) {
        this.title = title;
        this.teaching = teaching;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cancel = cancel;
        this.exercises = exercises;
    }
}
