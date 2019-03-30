package zunpiau.sqljudger.web.service;

import org.springframework.stereotype.Service;
import zunpiau.sqljudger.web.Repository.ExerciseConfigRepository;
import zunpiau.sqljudger.web.Repository.ExerciseRepository;
import zunpiau.sqljudger.web.controller.exception.NoEntityException;
import zunpiau.sqljudger.web.controller.response.ExerciseConfigVo;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.ExerciseConfig;
import zunpiau.sqljudger.web.domain.TestPaper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
public class ExerciseConfigService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseConfigRepository exerciseConfigRepository;

    public ExerciseConfigService(ExerciseRepository exerciseRepository,
            ExerciseConfigRepository exerciseConfigRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseConfigRepository = exerciseConfigRepository;
    }

    public List<ExerciseConfigVo> getExercisesForAnswer(TestPaper testPaper) {
        return getExercises(testPaper, id -> exerciseRepository.findByIdForAnswer(id));
    }

    public List<ExerciseConfigVo> getExercisesForStudent(TestPaper testPaper) {
        return getExercises(testPaper, id -> exerciseRepository.findByIdForStudent(id));
    }

    public List<ExerciseConfigVo> getExercises(TestPaper testPaper) {
        return getExercises(testPaper, id -> exerciseRepository.findById(id));
    }

    private List<ExerciseConfigVo> getExercises(TestPaper testPaper,
            Function<Long, Optional<Exercise>> exerciseFunction) {
        final Long[] exerciseConfigs = testPaper.getExerciseConfigs();
        List<ExerciseConfigVo> exercises = new ArrayList<>(exerciseConfigs.length);
        for (Long exerciseConfigId : exerciseConfigs) {
            final Optional<ExerciseConfig> optionalExerciseConfig = exerciseConfigRepository.findById(exerciseConfigId);
            final ExerciseConfig exerciseConfig = optionalExerciseConfig
                    .orElseThrow(() -> new NoEntityException(NoEntityException.STATUS_NO_EXERCISE_CONFIG));
            exercises.add(toVo(exerciseConfig, exerciseFunction));
        }
        return exercises;
    }

    private ExerciseConfigVo toVo(ExerciseConfig exerciseConfig, Function<Long, Optional<Exercise>> exerciseFunction) {
        final Optional<Exercise> optionalExercise = exerciseFunction.apply(exerciseConfig.getExercise());
        final Exercise exercise = optionalExercise
                .orElseThrow(() -> new NoEntityException(NoEntityException.STATUS_NO_EXERCISE));
        return ExerciseConfigVo.build(exerciseConfig, exercise);
    }

}
