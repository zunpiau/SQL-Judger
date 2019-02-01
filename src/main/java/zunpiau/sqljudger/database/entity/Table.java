package zunpiau.sqljudger.database.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {

    private String name;
    private List<Column> columns;
    private List<List<String>> data;
    @Nullable
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PrimaryKey primaryKey;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ForignKey> forignKeys;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Index> indexes;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Constraint> constraints;

}
