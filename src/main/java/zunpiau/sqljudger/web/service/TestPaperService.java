package zunpiau.sqljudger.web.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.Repository.ExerciseConfigRepository;
import zunpiau.sqljudger.web.Repository.TestPaperRepository;
import zunpiau.sqljudger.web.controller.request.TestPaperDto;
import zunpiau.sqljudger.web.controller.response.TestPaperVo;
import zunpiau.sqljudger.web.domain.ExerciseConfig;
import zunpiau.sqljudger.web.domain.Teacher;
import zunpiau.sqljudger.web.domain.TestPaper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TestPaperService {

    private final TestPaperRepository testPaperRepository;
    private final ExerciseConfigRepository exerciseConfigRepository;
    private final ExerciseConfigService exerciseConfigService;

    public TestPaperService(TestPaperRepository testPaperRepository,
            ExerciseConfigRepository exerciseConfigRepository,
            ExerciseConfigService exerciseConfigService) {
        this.testPaperRepository = testPaperRepository;
        this.exerciseConfigRepository = exerciseConfigRepository;
        this.exerciseConfigService = exerciseConfigService;
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

    public Iterable<TestPaperVo> findAll() {
        final List<TestPaper> testPapers = testPaperRepository.findAll();
        return testPapers.stream()
                .map(t -> TestPaperVo.build(t, exerciseConfigService.getExercises(t)))
                .collect(Collectors.toList());
    }

    @Transactional
    public TestPaper save(TestPaperDto testPaperDto, Long number) {
        TestPaper testPaper = new TestPaper();
        testPaper.setTeacher(new Teacher(number));
        testPaper.setTitle(testPaperDto.getTitle());
        final ExerciseConfig[] exerciseConfigs = testPaperDto.getExerciseConfigs();
        for (ExerciseConfig exerciseConfig : exerciseConfigs) {
            exerciseConfigRepository.save(exerciseConfig);
        }
        Long[] exerciseConfigIds = new Long[exerciseConfigs.length];
        for (int i = 0; i < exerciseConfigs.length; i++) {
            exerciseConfigIds[i] = exerciseConfigs[i].getId();
        }
        testPaper.setExerciseConfigs(exerciseConfigIds);
        testPaperRepository.save(testPaper);
        return testPaper;
    }

    public TestPaperVo getTestPaperForStudent(TestPaper testPaper) {
        return TestPaperVo.build(testPaper, exerciseConfigService.getExercisesForStudent(testPaper));
    }
}
