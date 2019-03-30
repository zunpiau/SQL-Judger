package zunpiau.sqljudger.web.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.ExerciseConfig;

@Data
@AllArgsConstructor
public class ExerciseConfigVo {

    private Long id;
    private Integer score;
    private Exercise exercise;

    public static ExerciseConfigVo build(ExerciseConfig exerciseConfig, Exercise exercise) {
        return new ExerciseConfigVo(exerciseConfig.getId(), exerciseConfig.getScore(), exercise);
    }

}
