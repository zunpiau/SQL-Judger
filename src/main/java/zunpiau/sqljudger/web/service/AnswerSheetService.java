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

    private final AnswerSheetRepository answerSheetRepository;
    private final AnswerRepository answerRepository;
    private final ExamService examService;

    public AnswerSheetService(AnswerSheetRepository answerSheetRepository,
            AnswerRepository answerRepository, ExamService examService) {
        this.answerSheetRepository = answerSheetRepository;
        this.answerRepository = answerRepository;
        this.examService = examService;
    }

    @Async("postExecutor")
    @Transactional
    public void save(Long exam, Set<Answer> answers, Long student) {
        examService.checkExam(exam, student);
        AnswerSheet answerSheet = new AnswerSheet(exam, student);
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
