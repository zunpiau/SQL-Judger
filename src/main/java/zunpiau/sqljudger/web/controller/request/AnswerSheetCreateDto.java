package zunpiau.sqljudger.web.controller.request;

import lombok.Data;
import zunpiau.sqljudger.web.domain.Answer;

import java.util.Set;

@Data
public class AnswerSheetCreateDto {

    private Long exam;
    private Set<Answer> answers;
}
