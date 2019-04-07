package zunpiau.sqljudger.web.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.Repository.AnswerRepository;
import zunpiau.sqljudger.web.Repository.AnswerSheetRepository;
import zunpiau.sqljudger.web.Repository.ExamRepository;
import zunpiau.sqljudger.web.Repository.StudentRepository;
import zunpiau.sqljudger.web.Repository.TeachingRepository;
import zunpiau.sqljudger.web.controller.exception.AuthException;
import zunpiau.sqljudger.web.controller.exception.ExamException;
import zunpiau.sqljudger.web.controller.exception.NoEntityException;
import zunpiau.sqljudger.web.controller.response.AnswerSheetDto;
import zunpiau.sqljudger.web.controller.response.ExamVo;
import zunpiau.sqljudger.web.controller.response.ExerciseConfigVo;
import zunpiau.sqljudger.web.controller.response.ScoreVo;
import zunpiau.sqljudger.web.domain.Answer;
import zunpiau.sqljudger.web.domain.AnswerSheet;
import zunpiau.sqljudger.web.domain.Clazz;
import zunpiau.sqljudger.web.domain.Exam;
import zunpiau.sqljudger.web.domain.Student;
import zunpiau.sqljudger.web.domain.Teaching;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.ArrayList;
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
    private final StudentRepository studentRepository;
    private final AnswerSheetRepository answerSheetRepository;
    private final ExamRepository examRepository;
    private final CorrectService correctService;
    private final TestPaperService testPaperService;
    private final ExerciseConfigService exerciseConfigService;
    private final AnswerRepository answerRepository;

    @Autowired
    public ExamService(ExamRepository examRepository,
            TeachingRepository teachingRepository, StudentRepository studentRepository,
            AnswerSheetRepository answerSheetRepository,
            CorrectService correctService, TestPaperService testPaperService,
            ExerciseConfigService exerciseConfigService,
            AnswerRepository answerRepository) {
        this.examRepository = examRepository;
        this.teachingRepository = teachingRepository;
        this.studentRepository = studentRepository;
        this.answerSheetRepository = answerSheetRepository;
        this.correctService = correctService;
        this.testPaperService = testPaperService;
        this.exerciseConfigService = exerciseConfigService;
        this.answerRepository = answerRepository;
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

    public ScoreVo getScore(Long examId, Long student) {
        Exam exam = findByIdAndStudent(examId, student);
        final Integer score = exam.getTestPaper().getScore();
        final AnswerSheet answerSheet = answerSheetRepository.findByExamAndStudent(examId, student);
        if (answerSheet == null) {
            return new ScoreVo(examId, score, null);
        }
        return new ScoreVo(examId, score, answerSheet.getScore());
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

    public boolean cancelExam(Long examId, Long teacherNumber) {
        return setStatus(examId, teacherNumber, Exam.STATUS_CANCEL);
    }

    public boolean setScoreViewable(Long examId, Long teacherNumber) {
        return setStatus(examId, teacherNumber, Exam.STATUS_REVIEWED);
    }

    @Transactional
    public boolean setStatus(Long examId, Long teacherNumber, int status) {
        findByIdAndTeacher(examId, teacherNumber);
        examRepository.setStatus(examId, status);
        return true;
    }

    public List<Student> getStudents(Long id, Long teacher) {
        final Exam exam = findByIdAndTeacher(id, teacher);
        final Long clazz = exam.getTeaching().getClazz().getId();
        return studentRepository.findAllByClazz_Id(clazz);
    }

    public List<ExerciseConfigVo> getExercise(Long id, Long teacher) {
        final Exam exam = findByIdAndTeacher(id, teacher);
        return exerciseConfigService.getExercises(exam.getTestPaper());
    }


    public List<AnswerSheetDto> getAnswerSheet(Long id, Long teacher) {
        final Exam exam = findByIdAndTeacher(id, teacher);
        if (exam.getStatus() != Exam.STATUS_CORRECTED) {
            throw new ExamException(STATUS_NON_CORRECT);
        }
        final Long clazz = exam.getTeaching().getClazz().getId();
        final Long[] exerciseConfigs = exam.getTestPaper().getExerciseConfigs();
        final List<Student> students = studentRepository.findAllByClazz_Id(clazz);
        List<AnswerSheetDto> dtos = new ArrayList<>(students.size());
        for (Student s : students) {
            AnswerSheetDto answerSheetDto = new AnswerSheetDto(s.getNumber(), s.getName(), exam.getId());
            final AnswerSheet answerSheet = answerSheetRepository.findByExamAndStudent(exam.getId(), s.getNumber());
            if (answerSheet != null) {
                final List<Answer> answers = new ArrayList<>(exerciseConfigs.length);
                for (Long exerciseConfig : exerciseConfigs) {
                    final Answer answer = answerRepository
                            .findByExerciseConfigAndAnswerSheet(exerciseConfig, answerSheet.getId());
                    answers.add(answer);
                }
                answerSheetDto.setScore(answerSheet.getScore());
                answerSheetDto.setAnswers(answers);
                answerSheetDto.setAnswerSheet(answerSheet.getId());
            }
            dtos.add(answerSheetDto);
        }
        return dtos;
    }

    @Transactional
    public int updateScore(Long answerSheet, List<Answer> answers) {
        int total = 0;
        for (Answer answer : answers) {
            total += answer.getScore();
            answerRepository.updateScore(answer.getId(), answer.getScore());
        }
        answerSheetRepository.setScore(answerSheet, total);
        return total;
    }

    @Async("examExecutor")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Future<Void> correctAll(Exam exam) {
        log.info("correct examID: {}", exam.getId());
        long start = System.currentTimeMillis();
        final List<AnswerSheet> answerSheets = answerSheetRepository.findAllByExam(exam.getId());
        if (!answerSheets.isEmpty()) {
            final List<ExerciseConfigVo> exercises = exerciseConfigService.getExercisesForAnswer(exam.getTestPaper());
            final Map<Long, ExerciseConfigVo> exerciseMap = exercises.stream()
                    .collect(Collectors.toMap(e -> e.getId(), Function.identity()));
            examRepository.setStatus(exam.getId(), Exam.STATUS_CORRECTING);
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
        }
        examRepository.setStatus(exam.getId(), Exam.STATUS_CORRECTED);
        log.info("correct examID: {}, answersheets: {}, time: {}",
                exam.getId(), answerSheets.size(), System.currentTimeMillis() - start);
        return AsyncResult.forValue(null);
    }

    public boolean isFinish(Exam exam) {
        return exam.getEndTime() + 300 <= Instant.now().getEpochSecond();
    }

    public boolean isNonstart(Exam exam) {
        return exam.getStartTime() > Instant.now().getEpochSecond();
    }

    public ExamVo getExamForStudent(Long examId, Long student) {
        final Exam exam = checkExam(examId, student);
        return ExamVo.build(exam, testPaperService.getTestPaperForStudent(exam.getTestPaper()));
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

    public XSSFWorkbook exportScore(Long id, Long number) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("姓名");
        header.createCell(1).setCellValue("学号");
        header.createCell(2).setCellValue("成绩");
        final Exam exam = findByIdAndTeacher(id, number);
        if (exam.getStatus() != Exam.STATUS_REVIEWED) {
            throw new ExamException(STATUS_NON_CORRECT);
        }
        final Long clazz = exam.getTeaching().getClazz().getId();
        final List<Student> students = studentRepository.findAllByClazz_Id(clazz);
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            final AnswerSheet answerSheet = answerSheetRepository.findByExamAndStudent(exam.getId(), s.getNumber());
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(s.getName());
            row.createCell(1).setCellValue(s.getNumber());
            if (answerSheet != null) {
                row.createCell(2).setCellValue(answerSheet.getScore());
            } else {
                row.createCell(2).setCellValue("缺考");
            }
        }
        return workbook;
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
