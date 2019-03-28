package zunpiau.sqljudger.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Exam implements Serializable {

    private static final long serialVersionUID = 1115574550960958188L;
    public static final int STATUS_NOMRAL = 0;
    public static final int STATUS_CANCEL = 1;
    public static final int STATUS_CORRECTING = 2;
    public static final int STATUS_CORRECTED = 3;
    public static final int STATUS_REVIEWED = 4;

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
    private Integer status = 0;
    @Type(type = "zunpiau.sqljudger.web.domain.LongArrayUserType")
    private Long[] exercises;

    public Exam(String title, Teaching teaching, Long startTime, Long endTime, Integer status,
            Long[] exercises) {
        this.title = title;
        this.teaching = teaching;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.exercises = exercises;
    }

    public Exam(Long id) {
        this.id = id;
    }
}
