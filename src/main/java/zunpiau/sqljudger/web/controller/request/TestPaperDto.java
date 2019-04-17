package zunpiau.sqljudger.web.controller.request;

import lombok.Data;
import zunpiau.sqljudger.web.domain.ExerciseConfig;

@Data
public class TestPaperDto {

    private Long id;
    private String title;
    private ExerciseConfig[] exerciseConfigs;

}
