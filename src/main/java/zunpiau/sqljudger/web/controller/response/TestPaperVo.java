package zunpiau.sqljudger.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import zunpiau.sqljudger.web.domain.Teacher;
import zunpiau.sqljudger.web.domain.TestPaper;

import java.util.List;

@Data
@AllArgsConstructor
public class TestPaperVo {

    private Long id;
    private String title;
    private Teacher teacher;
    private Integer score;
    private boolean used;
    private List<ExerciseConfigVo> exerciseConfigs;

    public static TestPaperVo build(TestPaper t, List<ExerciseConfigVo> exerciseConfigs, boolean used) {
        return new TestPaperVo(t.getId(), t.getTitle(), t.getTeacher(), t.getScore(), used, exerciseConfigs);
    }

}
