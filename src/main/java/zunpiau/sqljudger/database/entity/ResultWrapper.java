package zunpiau.sqljudger.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.RowSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultWrapper {

    RowSet rowSet;
    int updateConut;

}
