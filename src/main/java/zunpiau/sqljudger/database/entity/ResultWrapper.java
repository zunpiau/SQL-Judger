package zunpiau.sqljudger.database.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResultWrapper {

    DataSet dataSet;
    List<SimpleTable> tables;

    public ResultWrapper(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public ResultWrapper(List<SimpleTable> tables) {
        this.tables = tables;
    }
}
