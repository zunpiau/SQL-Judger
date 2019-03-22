package zunpiau.sqljudger.database.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResultWrapper {

    DataSet dataSet;
    List<SimpleTable> tables;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String exception;

    public ResultWrapper(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public ResultWrapper(List<SimpleTable> tables) {
        this.tables = tables;
    }

    public ResultWrapper(String exception) {
        this.exception = exception;
    }
}
