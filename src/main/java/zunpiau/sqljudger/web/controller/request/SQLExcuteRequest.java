package zunpiau.sqljudger.web.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SQLExcuteRequest {

    private String schema;
    private String excute;
    private boolean rowOrder;
    private boolean columnOrder;

}
