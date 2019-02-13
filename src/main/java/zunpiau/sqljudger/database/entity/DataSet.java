package zunpiau.sqljudger.database.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSet {

    private List<String> headers;
    private List<List<String>> data;

}
