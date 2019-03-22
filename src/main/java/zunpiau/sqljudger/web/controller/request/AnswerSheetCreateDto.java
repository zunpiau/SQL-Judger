package zunpiau.sqljudger.web.controller.request;

import lombok.Data;
import zunpiau.sqljudger.web.domain.Answer;
import zunpiau.sqljudger.web.domain.AnswerSheet;

import java.util.Set;

@Data
public class AnswerSheetCreateDto {

    private AnswerSheet answerSheet;
    private Set<Answer> answers;
}
