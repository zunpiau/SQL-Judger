package zunpiau.sqljudger.web.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(indexes = @Index(name = "teacher_name_idx", columnList = "name"))
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Teacher extends User implements Serializable {

    private static final long serialVersionUID = 1503799770930337669L;

}
