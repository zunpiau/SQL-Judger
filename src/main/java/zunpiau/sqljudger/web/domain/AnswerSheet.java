package zunpiau.sqljudger.web.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"exam_id", "student_number"}))
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerSheet implements Serializable {

    private static final long serialVersionUID = -8791912515407829578L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(nullable = false, name = "exam_id")
    private Long exam;

    @Column(nullable = false, name = "student_number")
    private Long student;

    private Integer score;

    public AnswerSheet(Long id, Integer score) {
        this.id = id;
        this.score = score;
    }

    public AnswerSheet(Long exam, Long student) {
        this.exam = exam;
        this.student = student;
    }
}
