package zunpiau.sqljudger.web.controller.request;

import lombok.Data;
import zunpiau.sqljudger.web.domain.ExerciseType;

import java.util.Map;

@Data
public class ComposeTestPaperRequest {

    private String title;
    private int score;
    private Map<ExerciseType, Integer> amounts;

}
