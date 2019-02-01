package zunpiau.sqljudger.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Index {

    private String name;
    private List<String> columns;
    private boolean unique;
    private int type;

}
