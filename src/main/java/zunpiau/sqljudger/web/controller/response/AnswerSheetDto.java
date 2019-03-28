package zunpiau.sqljudger.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import zunpiau.sqljudger.web.domain.AnswerSheet;

@Data
@AllArgsConstructor
public class AnswerSheetDto {

    private Long number;
    private String name;
    private AnswerSheet answerSheet;

}
