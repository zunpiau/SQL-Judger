package zunpiau.sqljudger.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zunpiau.sqljudger.database.entity.ResultWrapper;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"answersheet_id", "exercise_config_id"}))
public class Answer implements Serializable {

    private static final long serialVersionUID = 4886508888579456004L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(nullable = false, name = "exercise_config_id")
    private Long exerciseConfig;

    @Column(length = 2048)
    private String inputSQL;

    @Convert(converter = ResultWrapperJsonConverter.class)
    @Column(length = 2048)
    private ResultWrapper inputData;

    private Integer score = 0;

    @Column(name = "answersheet_id")
    private Long answerSheet;

    public Answer(Long exerciseConfig, String inputSQL, ResultWrapper inputData, Long answerSheet) {
        this.exerciseConfig = exerciseConfig;
        this.inputSQL = inputSQL;
        this.inputData = inputData;
        this.answerSheet = answerSheet;
    }
}
