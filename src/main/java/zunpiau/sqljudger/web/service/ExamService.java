package zunpiau.sqljudger.web.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.Repository.AnswerSheetRepository;
import zunpiau.sqljudger.web.Repository.ExamRepository;
import zunpiau.sqljudger.web.Repository.ExerciseRepository;
import zunpiau.sqljudger.web.Repository.StudentRepository;
import zunpiau.sqljudger.web.Repository.TeachingRepository;
import zunpiau.sqljudger.web.controller.exception.AuthException;
import zunpiau.sqljudger.web.controller.exception.ExamException;
import zunpiau.sqljudger.web.controller.exception.NoEntityException;
import zunpiau.sqljudger.web.domain.AnswerSheet;
import zunpiau.sqljudger.web.domain.Clazz;
import zunpiau.sqljudger.web.domain.Exam;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.Student;
import zunpiau.sqljudger.web.domain.Teaching;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

import static zunpiau.sqljudger.web.controller.exception.ExamException.*;

@Service
@Slf4j
public class ExamService {

    private final TeachingRepository teachingRepository;
    private final ExerciseRepository exerciseRepository;
    private final StudentRepository studentRepository;
    private final AnswerSheetRepository answerSheetRepository;
    private final ExamRepository examRepository;
    private final CorrectService correctService;

    @Autowired
    public ExamService(ExamRepository examRepository,
            TeachingRepository teachingRepository,
            ExerciseRepository exerciseRepository, StudentRepository studentRepository,
            AnswerSheetRepository answerSheetRepository,
            CorrectService correctService) {
        this.examRepository = examRepository;
        this.teachingRepository = teachingRepository;
        this.exerciseRepository = exerciseRepository;
        this.studentRepository = studentRepository;
        this.answerSheetRepository = answerSheetRepository;
        this.correctService = correctService;
    }

    @Transactional
    public Exam save(Exam exam, Long teacher) {
        final Optional<Teaching> optionalTeaching = teachingRepository
                .findByClazz_IdAndTeacher_Number(exam.getTeaching().getClazz().getId(), teacher);
        final Teaching teaching = optionalTeaching
                .orElseThrow(() -> new NoEntityException(NoEntityException.STATUS_NO_TEACHING));
        exam.setTeaching(teaching);
        examRepository.save(exam);
        return exam;
    }

    public Exam findByIdAndTeacher(Long id, Long teacher) {
        final Optional<Exam> optionalExam = examRepository.findByIdAndTeaching_Teacher_Number(id, teacher);
        return optionalExam.orElseThrow(() -> new NoEntityException(NoEntityException.STATUS_NO_EXAM));
    }

    @Transactional
    public List<Exam> findByTeacher(Long teacher) {
        return examRepository.findAllByTeaching_Teacher_Number(teacher);
    }

    @Transactional
    public List<Exam> findByStudent(Long number) {
        Optional<Student> optionalStudent = studentRepository.findById(number);
        if (optionalStudent.isPresent()) {
            Clazz clazz = optionalStudent.get().getClazz();
            final Long teaching = teachingRepository.findByClazz_Id(clazz.getId()).getId();
            return examRepository.findAllByTeaching_Id(teaching);
        }
        return Collections.emptyList();
    }

    @Nullable
    @Transactional
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Exam findByIdAndStudent(Long id, Long number) {
        Student student = studentRepository.findById(number).get();
        final Optional<Exam> optionalExam = examRepository.findById(id);
        if (!optionalExam.isPresent()) {
            throw new NoEntityException(NoEntityException.STATUS_NO_EXAM);
        }
        final Exam exam = optionalExam.get();
        final Teaching teaching = exam.getTeaching();
        if (!teaching.getClazz().getId().equals(student.getClazz().getId())) {
            throw new AuthException(AuthException.STATUS_STUDENT);
        }
        return exam;
    }

    @Transactional
    public boolean cancelExam(Long examId, Long teacherNumber) {
        findByIdAndTeacher(examId, teacherNumber);
        examRepository.setStatus(examId, Exam.STATUS_CANCEL);
        return true;
    }

    public List<AnswerSheet> getAnswerSheet(Long id, Long teacher) {
        final Exam exam = findByIdAndTeacher(id, teacher);
        if (exam.getStatus() != Exam.STATUS_CORRECTED) {
            throw new ExamException(STATUS_NON_CORRECT);
        }
        return answerSheetRepository.findAllByExam(id);
    }

    @Async("examExecutor")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<Void> correctAll(Long examId, Long teacher) {
        long start = System.currentTimeMillis();
        final Exam exam = findByIdAndTeacher(examId, teacher);
        final List<AnswerSheet> answerSheets = answerSheetRepository.findAllByExam(examId);
        final List<Exercise> exercises = exerciseRepository.findAllByIdForAnswer(exam.getExercises());
        final Map<Long, Exercise> exerciseMap = exercises.stream()
                .collect(Collectors.toMap(e -> e.getId(), Function.identity()));
        examRepository.setStatus(examId, Exam.STATUS_CORRECTING);
        CountDownLatch answerSheetLatch = new CountDownLatch(answerSheets.size());
        for (AnswerSheet answerSheet : answerSheets) {
            correctService.correctAsync(answerSheet, answerSheetLatch, exerciseMap);
        }
        try {
//            TimeUnit.SECONDS.sleep(10);
            answerSheetLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        examRepository.setStatus(examId, Exam.STATUS_CORRECTED);
        log.info("correct examID: {}, answersheets: {}, exercises: {}, time: {}",
                exam.getId(), answerSheets.size(), exercises.size(), System.currentTimeMillis() - start);
        return AsyncResult.forValue(null);
    }

    public boolean isFinish(Exam exam) {
        return exam.getEndTime() + 300 <= Instant.now().getEpochSecond();
    }

    public boolean isNonstart(Exam exam) {
        return exam.getStartTime() > Instant.now().getEpochSecond();
    }

    public Exam checkExam(Long examId, Long student) {
        Exam exam = findByIdAndStudent(examId, student);
        if (exam.getStatus() == Exam.STATUS_CANCEL) {
            throw new ExamException(STATUS_CANCELED);
        }
        if (isFinish(exam)) {
            throw new ExamException(STATUS_FINISHED);
        }
        if (isNonstart(exam)) {
            throw new ExamException(STATUS_NON_START);
        }
        if (answerSheetRepository.existsByExamAndStudent(examId, student)) {
            throw new ExamException(STATUS_ANSWERED);
        }
        return exam;
    }

    private void checkTeaching(Long teachingId, Long teacher) {
        final Optional<Teaching> optionalTeaching = teachingRepository.findById(teachingId);
        if (!optionalTeaching.isPresent()) {
            throw new NoEntityException(NoEntityException.STATUS_NO_TEACHING);
        }
        final Teaching teaching = optionalTeaching.get();
        if (!teaching.getTeacher().getNumber().equals(teacher)) {
            throw new AuthException(AuthException.STATUS_TEACHER);
        }
    }

}
