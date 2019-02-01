package zunpiau.sqljudger.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Constraint {

    private String name;
    private String condition;
    private String comment;

}
