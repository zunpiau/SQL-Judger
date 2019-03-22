package zunpiau.sqljudger.web.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.Repository.AnswerRepository;
import zunpiau.sqljudger.web.Repository.AnswerSheetRepository;
import zunpiau.sqljudger.web.controller.exception.NoEntityException;
import zunpiau.sqljudger.web.domain.Answer;
import zunpiau.sqljudger.web.domain.AnswerSheet;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AnswerSheetService {

    private final JdbcService jdbcService;
    private final AnswerSheetRepository answerSheetRepository;
    private final AnswerRepository answerRepository;
    private final ExamService examService;

    public AnswerSheetService(JdbcService jdbcService, AnswerSheetRepository answerSheetRepository,
            AnswerRepository answerRepository, ExamService examService) {
        this.jdbcService = jdbcService;
        this.answerSheetRepository = answerSheetRepository;
        this.answerRepository = answerRepository;
        this.examService = examService;
    }

    @Async("postExecutor")
    @Transactional
    public void save(AnswerSheet answerSheet, Set<Answer> answers, Long student) {
        examService.checkExam(answerSheet.getExam(), student);
        answerSheet.setStudent(student);
        answerSheetRepository.save(answerSheet);
        for (Answer answer : answers) {
            answer.setAnswerSheet(answerSheet.getId());
            answerRepository.save(answer);
        }
    }

    @Transactional
    public List<AnswerSheet> findAllByExam_Id(Long exam, Long teacher) {
        final Optional<AnswerSheet> byId = answerSheetRepository.findById(exam);
        final AnswerSheet answerSheet = byId
                .orElseThrow(() -> new NoEntityException(NoEntityException.STATUS_NO_ANSWERSHEET));
        examService.findByIdAndTeacher(answerSheet.getExam(), teacher);
        return answerSheetRepository.findAllByExam(exam);
    }

}
