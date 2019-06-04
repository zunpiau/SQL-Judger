package zunpiau.sqljudger.database.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(exclude = "formated")
@NoArgsConstructor
public class ResultWrapper {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String formated;

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
