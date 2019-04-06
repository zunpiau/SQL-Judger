package zunpiau.sqljudger.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import zunpiau.sqljudger.web.domain.Answer;

import java.util.List;

@Data
@AllArgsConstructor
public class AnswerSheetDto {

    private Long number;
    private String name;
    private Long exam;
    private Integer score;
    private Long answerSheet;
    private List<Answer> answers;

    public AnswerSheetDto(Long number, String name, Long exam) {
        this.number = number;
        this.name = name;
        this.exam = exam;
    }
}
