package zunpiau.sqljudger.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import zunpiau.sqljudger.web.domain.Exam;

@Data
@AllArgsConstructor
public class ExamVo {

    private Long id;
    private String title;
    private Long startTime;
    private Long endTime;
    private Integer status = 0;
    private TestPaperVo testPaper;

    public static ExamVo build(Exam exam, TestPaperVo testPaperVo) {
        return new ExamVo(exam.getId(), exam.getTitle(), exam.getStartTime(), exam.getEndTime(), exam.getStatus(),
                testPaperVo);
    }
}
