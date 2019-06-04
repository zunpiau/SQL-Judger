package zunpiau.sqljudger.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.database.entity.ResultWrapper;
import zunpiau.sqljudger.web.Repository.AnswerRepository;
import zunpiau.sqljudger.web.Repository.AnswerSheetRepository;
import zunpiau.sqljudger.web.Repository.ExamRepository;
import zunpiau.sqljudger.web.controller.exception.NoEntityException;
import zunpiau.sqljudger.web.controller.response.ExerciseConfigVo;
import zunpiau.sqljudger.web.domain.Answer;
import zunpiau.sqljudger.web.domain.AnswerSheet;
import zunpiau.sqljudger.web.domain.Exam;
import zunpiau.sqljudger.web.domain.Exercise;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

@Slf4j
@Service
public class ExamServiceHelper {

    private final ExamRepository examRepository;
    private final JdbcService jdbcService;
    private final AnswerSheetRepository answerSheetRepository;
    private final AnswerRepository answerRepository;

    public ExamServiceHelper(ExamRepository examRepository, JdbcService jdbcService,
            AnswerSheetRepository answerSheetRepository,
            AnswerRepository answerRepository) {
        this.examRepository = examRepository;
        this.jdbcService = jdbcService;
        this.answerSheetRepository = answerSheetRepository;
        this.answerRepository = answerRepository;
    }

    @Async("answersheetExecutor")
    @Transactional
    public Future<AnswerSheet> correctAnswerSheet(AnswerSheet answerSheet, CountDownLatch latch,
            Map<Long, ExerciseConfigVo> exerciseMap) {
        log.info("correct answersheetID: {}", answerSheet.getId());
        final long start = System.currentTimeMillis();
        final List<Answer> answers = answerRepository.findAllByAnswerSheet(answerSheet.getId());
        log.info("get answers:{}", answers);
        try {
            int totalScore = 0;
            for (Answer answer : answers) {
                correctAnswer(answer, exerciseMap.get(answer.getExerciseConfig()));
                answerRepository.merger(answer);
                totalScore += answer.getScore();
            }
            answerSheet.setScore(totalScore);
            answerSheetRepository.setScore(answerSheet.getId(), totalScore);
        } finally {
            latch.countDown();
        }
        log.info("correct answersheetID: {}, answers: {}, time: {}",
                answerSheet.getId(), answers.size(), System.currentTimeMillis() - start);
        return AsyncResult.forValue(answerSheet);
    }

    public Answer correctAnswer(Answer answer, ExerciseConfigVo exerciseConfig) {
        log.info("correctAnswer answerID: {}", answer.getId());
        final long start = System.currentTimeMillis();
        final Exercise exercise = exerciseConfig.getExercise();
        final ResultWrapper wrapper;
        final String formatedSQL = JdbcService.formatSQL(answer.getInputSQL());
        boolean equal = false;
        if (formatedSQL.equals(exercise.getExpectedData().getFormated())) {
            equal = true;
            wrapper = new ResultWrapper();
            wrapper.setFormated(formatedSQL);
        } else {
            wrapper = jdbcService.excute(exercise.getInputSQL(), answer.getInputSQL(),
                    exercise.isRowOrder(), exercise.isColumnOrder());
        }
        log.info("correctAnswer answerID: {}, result:{}", answer.getId(), wrapper);
        answer.setInputData(wrapper);
        if (equal || wrapper.equals(exercise.getExpectedData())) {
            answer.setScore(exerciseConfig.getScore());
        } else {
            answer.setScore(0);
        }
        log.info("correctAnswer answerID: {}, time: {}", answer.getId(), System.currentTimeMillis() - start);
        return answer;
    }

    @Cacheable(cacheNames = "exam", key = "#id")
    public Exam findById(Long id) {
        return examRepository.findById(id).orElseThrow(() -> new NoEntityException(NoEntityException.STATUS_NO_EXAM));
    }

}
