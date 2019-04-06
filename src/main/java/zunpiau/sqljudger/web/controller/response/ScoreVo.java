package zunpiau.sqljudger.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoreVo {

    private Long exam;
    private Integer total;
    private Integer score;

}
