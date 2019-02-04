package zunpiau.sqljudger.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exercise implements Serializable {

    private static final int TYPE_OUTPUT = 1;
    private static final int TYPE_SCHEMA = 2;
    private static final long serialVersionUID = -5806651385169097504L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 512, nullable = false)
    private String title;
    @Column(length = 2048, nullable = false)
    private String description;
    @Column(length = 2048)
    private String inputSQL;
    @Column(length = 2048)
    private String input;
    @Column(length = 2048)
    private String s;
    @Column(length = 2048, nullable = false)
    private String expected;
    @Column(columnDefinition = "smallint", nullable = false)
    private Integer type;
    private int score;
}
