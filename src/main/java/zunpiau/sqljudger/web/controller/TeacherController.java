package zunpiau.sqljudger.web.controller;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zunpiau.sqljudger.web.BaseResponse;
import zunpiau.sqljudger.web.Repository.ExerciseRepository;
import zunpiau.sqljudger.web.Repository.TeacherRepository;
import zunpiau.sqljudger.web.Repository.TeachingRepository;
import zunpiau.sqljudger.web.controller.request.SQLExcuteRequest;
import zunpiau.sqljudger.web.controller.request.TestPaperDto;
import zunpiau.sqljudger.web.controller.request.UpdateScoreDto;
import zunpiau.sqljudger.web.domain.Exam;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.Teacher;
import zunpiau.sqljudger.web.security.JwtInterceptor;
import zunpiau.sqljudger.web.service.AnswerSheetService;
import zunpiau.sqljudger.web.service.ExamService;
import zunpiau.sqljudger.web.service.JdbcService;
import zunpiau.sqljudger.web.service.TestPaperService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/teacher/")
public class TeacherController {

    private final ExerciseRepository exerciseRepository;
    private final TeachingRepository teachingRepository;
    private final TeacherRepository teacherRepository;
    private final TestPaperService testPaperService;
    private final ExamService examService;
    private final AnswerSheetService answerSheetService;
    private final JdbcService jdbcService;

    @Autowired
    public TeacherController(ExerciseRepository exerciseRepository, TeachingRepository teachingRepository,
            TeacherRepository teacherRepository,
            TestPaperService testPaperService, ExamService examService,
            AnswerSheetService answerSheetService, JdbcService jdbcService) {
        this.exerciseRepository = exerciseRepository;
        this.teachingRepository = teachingRepository;
        this.teacherRepository = teacherRepository;
        this.testPaperService = testPaperService;
        this.examService = examService;
        this.answerSheetService = answerSheetService;
        this.jdbcService = jdbcService;
    }

    @GetMapping("profile")
    public BaseResponse<?> getProfile(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        final Optional<Teacher> optionalTeacher = teacherRepository.findById(number);
        return optionalTeacher.map(BaseResponse::ok)
                .orElseGet(() -> new BaseResponse<>(400));
    }

    @GetMapping("exercise")
    public BaseResponse<?> getExercise() {
        return BaseResponse.ok(exerciseRepository.findAll());
    }

    @PostMapping("exercise")
    public BaseResponse<?> addExercise(@RequestBody Exercise exercise,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        exercise.setTeacher(new Teacher(number));
        return BaseResponse.ok(exerciseRepository.save(exercise));
    }

    @DeleteMapping("exercise/{id}")
    public BaseResponse<?> deleteExercise(@PathVariable Long id,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        final int updateCount = exerciseRepository.deleteByIdAndTeacher(id, new Teacher(number));
        return BaseResponse.ok(updateCount > 0);
    }

    @GetMapping("testPaper")
    public BaseResponse<?> getTestPaper() {
        return BaseResponse.ok(testPaperService.findAll());
    }

    @PostMapping("testPaper")
    public BaseResponse<?> addTestPaper(@RequestBody TestPaperDto testPaperDto,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(testPaperService.save(testPaperDto, number));
    }

    @DeleteMapping("testPaper/{id}")
    public BaseResponse<?> deleteTestPaper(@PathVariable Long id,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        final int updateCount = testPaperService.delete(id, number);
        return BaseResponse.ok(updateCount > 0);
    }

    @PostMapping("exam")
    public BaseResponse<?> addExam(@RequestBody Exam exam, @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(examService.save(exam, number));
    }

    @GetMapping("exam/{id}")
    public BaseResponse<?> getExam(@PathVariable Long id, @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(examService.findByIdAndTeacher(id, number));
    }

    @PutMapping("exam/{id}/cancel")
    public BaseResponse<?> cancelExam(@PathVariable Long id,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(examService.cancelExam(id, number));
    }

    @PutMapping("exam/{id}/public")
    public BaseResponse<?> publicExam(@PathVariable Long id,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(examService.setScoreViewable(id, number));
    }

    @GetMapping("exam")
    public BaseResponse<?> getExam(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(examService.findByTeacher(number));
    }

    @PutMapping("exam/{id}/correct")
    public BaseResponse<?> correct(@PathVariable Long id, @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        final Exam exam = examService.findByIdAndTeacher(id, number);
        examService.correctAll(exam);
        return BaseResponse.ok();
    }

    @GetMapping("exam/{id}/exercise")
    public BaseResponse<?> getExercise(@PathVariable Long id,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(examService.getExercise(id, number));
    }

    @GetMapping("exam/{id}/student")
    public BaseResponse<?> getAnswersheet(@PathVariable Long id,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(examService.getAnswerSheet(id, number));
    }

    @GetMapping("exam/{id}/score/export")
    public void exportScore(HttpServletResponse response, @PathVariable Long id,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) throws IOException {
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        final XSSFWorkbook workbook = examService.exportScore(id, number);
        workbook.write(response.getOutputStream());
    }

    @GetMapping("answersheet/{id}/answers")
    public BaseResponse<?> getAnswer(@PathVariable Long id,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(answerSheetService.findAllByExam_Id(id, number));
    }

    @PutMapping("answersheet/{id}/answer")
    public BaseResponse<?> updateScore(@PathVariable Long id, @RequestBody UpdateScoreDto updateScoreDto) {
        return BaseResponse.ok(examService.updateScore(id, updateScoreDto.getAnswers()));
    }

    @GetMapping("teaching")
    public BaseResponse<?> getTeaching(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return BaseResponse.ok(teachingRepository.findAllByTeacher_Number(number));
    }

    @PostMapping("sql/retrieve")
    public BaseResponse<?> generatInput(@RequestBody String sql) throws SQLException {
        return BaseResponse.ok(jdbcService.excuteAndRetrieve(sql));
    }

    @PostMapping("sql/excute")
    public BaseResponse<?> generatInput(@RequestBody SQLExcuteRequest request) throws SQLException {
        return BaseResponse.ok(jdbcService.excute(request.getSchema(), request.getExcute()));
    }

    @ExceptionHandler({ConstraintViolationException.class, SQLException.class})
    public BaseResponse<?> handleSQLExcetion(Exception e) {
        return new BaseResponse<>(400, e.getMessage(), null);
    }

}
