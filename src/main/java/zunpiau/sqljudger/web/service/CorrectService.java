package zunpiau.sqljudger.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.database.entity.ResultWrapper;
import zunpiau.sqljudger.web.Repository.AnswerRepository;
import zunpiau.sqljudger.web.Repository.AnswerSheetRepository;
import zunpiau.sqljudger.web.controller.response.ExerciseConfigVo;
import zunpiau.sqljudger.web.domain.Answer;
import zunpiau.sqljudger.web.domain.AnswerSheet;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

@Service
@Slf4j
public class CorrectService {

    private final JdbcService jdbcService;
    private final AnswerSheetRepository answerSheetRepository;
    private final AnswerRepository answerRepository;

    public CorrectService(JdbcService jdbcService, AnswerSheetRepository answerSheetRepository,
            AnswerRepository answerRepository) {
        this.jdbcService = jdbcService;
        this.answerSheetRepository = answerSheetRepository;
        this.answerRepository = answerRepository;
    }

    @Async("answersheetExecutor")
    @Transactional
    public Future<AnswerSheet> correctAsync(AnswerSheet answerSheet, CountDownLatch latch,
            Map<Long, ExerciseConfigVo> exerciseMap) {
        log.info("correct answersheetID: {}", answerSheet.getId());
        final long start = System.currentTimeMillis();
        final List<Answer> answers = answerRepository.findAllByAnswerSheet(answerSheet.getId());
        try {
            int totalScore = 0;
            for (Answer answer : answers) {
                correct(answer, exerciseMap.get(answer.getExerciseConfig()));
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

    public Answer correct(Answer answer, ExerciseConfigVo exerciseConfig) {
        log.info("correct answerID: {}", answer.getId());
        final long start = System.currentTimeMillis();
        final ResultWrapper wrapper = jdbcService
                .excute(exerciseConfig.getExercise().getInputSQL(), answer.getInputSQL());
        answer.setInputData(wrapper);
        if (wrapper.equals(exerciseConfig.getExercise().getExpectedData())) {
            answer.setScore(exerciseConfig.getScore());
        } else {
            answer.setScore(0);
        }
        log.info("correct answerID: {}, time: {}", answer.getId(), System.currentTimeMillis() - start);
        return answer;
    }

}
