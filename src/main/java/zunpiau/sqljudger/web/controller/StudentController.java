package zunpiau.sqljudger.web.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zunpiau.sqljudger.web.BaseResponse;
import zunpiau.sqljudger.web.Repository.ExerciseRepository;
import zunpiau.sqljudger.web.Repository.StudentRepository;
import zunpiau.sqljudger.web.controller.request.AnswerSheetCreateDto;
import zunpiau.sqljudger.web.domain.Exam;
import zunpiau.sqljudger.web.domain.Student;
import zunpiau.sqljudger.web.security.JwtInterceptor;
import zunpiau.sqljudger.web.service.AnswerSheetService;
import zunpiau.sqljudger.web.service.ExamService;

import java.util.Optional;

@RestController
@RequestMapping("/student/")
public class StudentController {

    private final StudentRepository studentRepository;
    private final ExamService examService;
    private final ExerciseRepository exerciseRepository;
    private final AnswerSheetService answerSheetService;

    public StudentController(StudentRepository studentRepository, ExamService examService,
            ExerciseRepository exerciseRepository,
            AnswerSheetService answerSheetService) {
        this.studentRepository = studentRepository;
        this.examService = examService;
        this.exerciseRepository = exerciseRepository;
        this.answerSheetService = answerSheetService;
    }

    @GetMapping("profile")
    public BaseResponse<?> getProfile(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        final Optional<Student> optionalStudent = studentRepository.findById(number);
        return optionalStudent.map(BaseResponse::ok)
                .orElseGet(() -> new BaseResponse<>(400));
    }

    @GetMapping("exam")
    public ResponseEntity<?> getExam(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number) {
        return ResponseEntity.ok(examService.findByStudent(number));
    }

    @GetMapping("exam/{id}")
    public BaseResponse<?> getExam(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number,
            @PathVariable Long id) {
        return BaseResponse.ok(examService.checkExam(id, number));
    }

    @GetMapping("exam/{id}/exercise")
    public BaseResponse<?> getExercise(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number,
            @PathVariable Long id) {
        final Exam exam = examService.checkExam(id, number);
        return BaseResponse.ok(exerciseRepository.findAllById(exam.getExercises()));
    }

    @PostMapping(value = "exam/answer_sheet", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> postAnswerSheet(@RequestAttribute(JwtInterceptor.ATTR_NUMBER) Long number,
            @RequestBody AnswerSheetCreateDto dto) {
        answerSheetService.save(dto.getAnswerSheet(), dto.getAnswers(), number);
        return BaseResponse.ok();
    }

}
