package zunpiau.sqljudger.web.controller.request;

import lombok.Data;
import zunpiau.sqljudger.web.domain.Answer;

import java.util.List;

@Data
public class UpdateScoreDto {

    private Long number;
    private List<Answer> answers;

}
