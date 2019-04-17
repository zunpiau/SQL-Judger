package zunpiau.sqljudger.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zunpiau.sqljudger.web.BaseResponse;
import zunpiau.sqljudger.web.Repository.ClazzRepository;
import zunpiau.sqljudger.web.Repository.TeacherRepository;
import zunpiau.sqljudger.web.Repository.TeachingRepository;
import zunpiau.sqljudger.web.domain.Clazz;
import zunpiau.sqljudger.web.domain.Student;
import zunpiau.sqljudger.web.domain.Teacher;
import zunpiau.sqljudger.web.domain.Teaching;
import zunpiau.sqljudger.web.domain.User;
import zunpiau.sqljudger.web.service.StudentService;

@RestController
@RequestMapping("/admin/")
public class AdminController {

    private final TeacherRepository teacherRepository;
    private final StudentService studentService;
    private final ClazzRepository clazzRepository;
    private final TeachingRepository teachingRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(TeacherRepository teacherRepository,
            StudentService studentService,
            ClazzRepository clazzRepository, TeachingRepository teachingRepository,
            PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.studentService = studentService;
        this.clazzRepository = clazzRepository;
        this.teachingRepository = teachingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "teacher", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> addTeacher(@RequestBody Teacher teacher) {
        handlePassword(teacher);
        return BaseResponse.ok(teacherRepository.save(teacher));
    }

    @GetMapping("teacher")
    public ResponseEntity<?> getAllTeacher() {
        return ResponseEntity.ok(teacherRepository.findAll());
    }

    @GetMapping("teacher/{number}")
    public ResponseEntity<?> getTeacher(@PathVariable Long number) {
        return ResponseEntity.ok(teacherRepository.findById(number));
    }

    @DeleteMapping("teacher/{number}")
    public BaseResponse<?> deleteTeacher(@PathVariable Long number) {
        teacherRepository.deleteById(number);
        return BaseResponse.ok();
    }

    @PutMapping(value = "teacher", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> modifyTeacher(@RequestBody Teacher teacher) {
        handlePassword(teacher);
        return BaseResponse.ok(teacherRepository.merger(teacher));
    }

    @PostMapping(value = "student", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> addStudent(@RequestBody Student student) {
        handlePassword(student);
        return BaseResponse.ok(studentService.add(student));
    }

    @PutMapping(value = "student", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> modifyStudent(@RequestBody Student student) {
        handlePassword(student);
        return BaseResponse.ok(studentService.update(student));
    }

    private void handlePassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    @GetMapping("student")
    public ResponseEntity<?> getAllStudent() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("student/{number}")
    public ResponseEntity<?> getStudent(@PathVariable Long number) {
        return ResponseEntity.ok(studentService.findById(number));
    }

    @DeleteMapping("student/{number}")
    public BaseResponse<?> deleteStudent(@PathVariable Long number) {
        studentService.deleteById(number);
        return BaseResponse.ok();
    }

    @PostMapping(value = "clazz", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> addClazz(@RequestBody Clazz clazz) {
        return BaseResponse.ok(clazzRepository.save(clazz));
    }

    @PutMapping(value = "clazz", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> modifyClazz(@RequestBody Clazz clazz) {
        return BaseResponse.ok(clazzRepository.merger(clazz));
    }

    @GetMapping("clazz")
    public ResponseEntity<?> getAllClazz() {
        return ResponseEntity.ok(clazzRepository.findAll());
    }

    @GetMapping("clazz/{id}")
    public ResponseEntity<?> getClazz(@PathVariable long id) {
        return ResponseEntity.ok(clazzRepository.findById(id));
    }

    @DeleteMapping("clazz/{id}")
    public BaseResponse<?> deleteClazz(@PathVariable long id) {
        clazzRepository.deleteById(id);
        return BaseResponse.ok();
    }

    @PostMapping(value = "teaching", consumes = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<?> addTeaching(@RequestBody Teaching teaching) {
        return BaseResponse.ok(teachingRepository.save(teaching));
    }

    @GetMapping("teaching")
    public ResponseEntity<?> getAllTeaching() {
        return ResponseEntity.ok(teachingRepository.findAll());
    }

    @GetMapping("teaching/{id}")
    public ResponseEntity<?> getTeaching(@PathVariable long id) {
        return ResponseEntity.ok(teachingRepository.findById(id));
    }

    @DeleteMapping("teaching/{id}")
    public BaseResponse<?> deleteTeaching(@PathVariable long id) {
        teachingRepository.deleteById(id);
        return BaseResponse.ok();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public BaseResponse<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new BaseResponse<>(400, e.getMessage(), null);
    }
}
