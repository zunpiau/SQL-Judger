package zunpiau.sqljudger.web.controller;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import zunpiau.sqljudger.web.domain.Exam;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.Teacher;
import zunpiau.sqljudger.web.security.JwtInterceptor;
import zunpiau.sqljudger.web.service.ExamService;
import zunpiau.sqljudger.web.service.JdbcService;

import java.sql.SQLException;
import java.util.Optional;

@RestController
@RequestMapping("/teacher/")
public class TeacherController {

    private final ExerciseRepository exerciseRepository;
    private final TeachingRepository teachingRepository;
    private final TeacherRepository teacherRepository;
    private final ExamService examService;
    private final JdbcService jdbcService;

    @Autowired
    public TeacherController(ExerciseRepository exerciseRepository, TeachingRepository teachingRepository,
            TeacherRepository teacherRepository, ExamService examService,
            JdbcService jdbcService) {
        this.exerciseRepository = exerciseRepository;
        this.teachingRepository = teachingRepository;
        this.teacherRepository = teacherRepository;
        this.examService = examService;
        this.jdbcService = jdbcService;
    }

    @GetMapping("profile")
    public BaseResponse<?> getProfile(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        final Optional<Teacher> optionalTeacher = teacherRepository.findById(number);
        return optionalTeacher.map(BaseResponse::ok)
                .orElseGet(() -> new BaseResponse<>(400));
    }

    @GetMapping("exercise")
    public ResponseEntity<?> getExercise() {
        return ResponseEntity.ok(exerciseRepository.findAll());
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

    @PostMapping("exam")
    public BaseResponse<?> addExam(@RequestBody Exam exam, @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        exam.getTeaching().setTeacher(new Teacher(number));
        return BaseResponse.ok(examService.save(exam, number));
    }

    @GetMapping("exam/{id}")
    public ResponseEntity<?> getExam(@PathVariable Long id, @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return ResponseEntity.ok(examService.findByIdAndTeacher(id, number));
    }

    @PutMapping("exam/{id}/cancel")
    public ResponseEntity<?> cancelExam(@PathVariable Long id,
            @RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return ResponseEntity.ok(examService.cancelExam(id, number));
    }

    @GetMapping("exam")
    public ResponseEntity<?> getExam(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return ResponseEntity.ok(examService.findByTeacher(number));
    }

    @GetMapping("teaching")
    public ResponseEntity<?> getTeaching(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return ResponseEntity.ok(teachingRepository.findTeachingsByTeacher(new Teacher(number)));
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
