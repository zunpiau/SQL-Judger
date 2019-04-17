package zunpiau.sqljudger.web.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.Repository.ExamRepository;
import zunpiau.sqljudger.web.Repository.ExerciseConfigRepository;
import zunpiau.sqljudger.web.Repository.ExerciseRepository;
import zunpiau.sqljudger.web.Repository.TestPaperRepository;
import zunpiau.sqljudger.web.controller.exception.NoEntityException;
import zunpiau.sqljudger.web.controller.request.TestPaperDto;
import zunpiau.sqljudger.web.controller.response.ExerciseConfigVo;
import zunpiau.sqljudger.web.controller.response.TestPaperVo;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.ExerciseConfig;
import zunpiau.sqljudger.web.domain.ExerciseType;
import zunpiau.sqljudger.web.domain.Teacher;
import zunpiau.sqljudger.web.domain.TestPaper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TestPaperService {

    public static final ExerciseConfig[] EXERCISE_CONFIGS = new ExerciseConfig[0];
    private final TestPaperRepository testPaperRepository;
    private final ExerciseConfigRepository exerciseConfigRepository;
    private final ExerciseConfigService exerciseConfigService;
    private final ExerciseRepository exerciseRepository;
    private final ExamRepository examRepository;

    public TestPaperService(TestPaperRepository testPaperRepository,
            ExerciseConfigRepository exerciseConfigRepository,
            ExerciseConfigService exerciseConfigService,
            ExerciseRepository exerciseRepository, ExamRepository examRepository) {
        this.testPaperRepository = testPaperRepository;
        this.exerciseConfigRepository = exerciseConfigRepository;
        this.exerciseConfigService = exerciseConfigService;
        this.exerciseRepository = exerciseRepository;
        this.examRepository = examRepository;
    }

    private static int getScore(ExerciseConfig[] exerciseConfigs) {
        int score = 0;
        for (ExerciseConfig exerciseConfig : exerciseConfigs) {
            score += exerciseConfig.getScore();
        }
        return score;
    }

    @Transactional
    public int delete(Long id, Long teacher) {
        final Optional<TestPaper> optionalTestPaper = testPaperRepository.findByIdAndTeacher_Number(id, teacher);
        if (!optionalTestPaper.isPresent()) {
            return 0;
        }
        final TestPaper testPaper = optionalTestPaper.get();
        exerciseConfigRepository.deleteByIds(Arrays.asList(testPaper.getExerciseConfigs()));
        return testPaperRepository.deleteByIdAndTeacher_Number(id, teacher);
    }

    public Iterable<TestPaperVo> findByTeacher(Long number) {
        final List<TestPaper> testPapers = testPaperRepository.findAllByTeacher_Number(number);
        return testPapers.stream()
                .map(t -> getVO(t))
                .collect(Collectors.toList());
    }

    @Transactional
    public TestPaperVo create(Map<ExerciseType, Integer> amounts, String title, Long number) {
        List<ExerciseConfig> exerciseConfigs = new ArrayList<>();
        for (Map.Entry<ExerciseType, Integer> entry : amounts.entrySet()) {
            final Integer amount = entry.getValue();
            if (amount == null || amount == 0) {
                continue;
            }
            final List<Exercise> byType = exerciseRepository.findAllByType(entry.getKey());
            Collections.shuffle(byType);
            for (int i = 0; i < amount && i < byType.size(); i++) {
                final Exercise exercise = byType.get(i);
                exerciseConfigs.add(new ExerciseConfig(null, exercise.getId(), exercise.getScore()));
            }
        }
        final TestPaper save = save(title, number, exerciseConfigs.toArray(EXERCISE_CONFIGS));
        return getVO(save);
    }

    @Transactional
    public TestPaper save(TestPaperDto testPaperDto, Long number) {
        return save(testPaperDto.getTitle(), number, testPaperDto.getExerciseConfigs());
    }

    @Transactional
    public TestPaperVo update(TestPaperDto testPaperDto, Long number) {
        final Optional<TestPaper> optionalTestPaper = testPaperRepository.findById(testPaperDto.getId());
        if (!optionalTestPaper.isPresent()) {
            throw new NoEntityException(NoEntityException.STATUS_NO_TESTPAPER);
        }
        final TestPaper testPaper = optionalTestPaper.get();
        exerciseConfigRepository.deleteByIds(Arrays.asList(testPaper.getExerciseConfigs()));
        final Long[] exercise = saveExercise(testPaperDto.getExerciseConfigs());
        testPaper.setExerciseConfigs(exercise);
        testPaper.setScore(getScore(testPaperDto.getExerciseConfigs()));
        testPaper.setTitle(testPaperDto.getTitle());
        testPaperRepository.merger(testPaper);
        return getVO(testPaper);
    }

    @Transactional
    public TestPaper save(String title, Long number, ExerciseConfig[] exerciseConfigs) {
        TestPaper testPaper = new TestPaper();
        testPaper.setTeacher(new Teacher(number));
        testPaper.setTitle(title);
        int score = getScore(exerciseConfigs);
        Long[] exerciseConfigIds = saveExercise(exerciseConfigs);
        testPaper.setScore(score);
        testPaper.setExerciseConfigs(exerciseConfigIds);
        testPaperRepository.save(testPaper);
        return testPaper;
    }

    @Cacheable(cacheNames = "testPaperForStudent", key = "#testPaper.getId()")
    public TestPaperVo getTestPaperForStudent(TestPaper testPaper) {
        return getVO(testPaper, exerciseConfigService.getExercisesForStudent(testPaper));
    }

    private TestPaperVo getVO(TestPaper t, List<ExerciseConfigVo> exerciseConfigVos) {
        return TestPaperVo.build(t, exerciseConfigVos, examRepository.existsByTestPaper_Id(t.getId()));
    }

    private TestPaperVo getVO(TestPaper t) {
        return getVO(t, exerciseConfigService.getExercises(t));
    }

    private Long[] saveExercise(ExerciseConfig[] exerciseConfigs) {
        Long[] exerciseConfigIds = new Long[exerciseConfigs.length];
        for (int i = 0; i < exerciseConfigs.length; i++) {
            exerciseConfigRepository.save(exerciseConfigs[i]);
            exerciseConfigIds[i] = exerciseConfigs[i].getId();
        }
        return exerciseConfigIds;
    }
}
