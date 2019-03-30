package zunpiau.sqljudger.web.domain;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class TestPaper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512, nullable = false)
    private String title;

    @JoinColumn(name = "teacher_number", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Teacher teacher;

    @Column(columnDefinition = "bigint[]")
    @Type(type = "zunpiau.sqljudger.web.domain.LongArrayUserType")
    private Long[] exerciseConfigs;

}
