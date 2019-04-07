package zunpiau.sqljudger.web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(indexes = @Index(columnList = "name"))
@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@NoArgsConstructor
public class Teacher extends User implements Serializable {

    private static final long serialVersionUID = 1503799770930337669L;

    public Teacher(Long number) {
        super(number);
    }
}
