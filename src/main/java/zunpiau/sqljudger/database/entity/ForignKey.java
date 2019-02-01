package zunpiau.sqljudger.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForignKey {

    private String name;
    private String column;
    private String forignKeyTable;
    private String forignKeyColumn;
    private int updateRule;
    private int deleteRule;

}
