package zunpiau.sqljudger.web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(indexes = @Index(columnList = "name"))
@Data
@ToString(exclude = "clazz", callSuper = true)
@EqualsAndHashCode(exclude = "clazz")
@NoArgsConstructor
public class Student extends User implements Serializable {

    private static final long serialVersionUID = 1503799770930337669L;
    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Clazz clazz;

    public Student(Long number) {
        super(number);
    }
}
