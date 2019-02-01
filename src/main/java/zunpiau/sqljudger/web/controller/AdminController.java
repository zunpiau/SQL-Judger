package zunpiau.sqljudger.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zunpiau.sqljudger.web.Repository.StudentRepository;
import zunpiau.sqljudger.web.Repository.TeacherRepository;
import zunpiau.sqljudger.web.domain.Student;
import zunpiau.sqljudger.web.domain.Teacher;

@RestController
@RequestMapping("/admin/")
public class AdminController {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(TeacherRepository teacherRepository,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping(value = "teacher", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addTeacher(@RequestBody Teacher teacher) {
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacherRepository.save(teacher);
        return ResponseEntity.ok().build();
    }

    @GetMapping("teacher")
    public ResponseEntity<?> getAllTeacher() {
        return ResponseEntity.ok(teacherRepository.findAll());
    }

    @GetMapping("teacher/{number}")
    public ResponseEntity<?> getTeacher(@PathVariable String number) {
        return ResponseEntity.ok(teacherRepository.findById(number));
    }

    @DeleteMapping("teacher/{number}")
    public ResponseEntity<?> deleteTeacher(@PathVariable String number) {
        teacherRepository.deleteById(number);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "student", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addStudent(@RequestBody Student student) {
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        studentRepository.save(student);
        return ResponseEntity.ok().build();
    }

    @GetMapping("student")
    public ResponseEntity<?> getAllStudent() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("student/{number}")
    public ResponseEntity<?> getStudent(@PathVariable String number) {
        return ResponseEntity.ok(studentRepository.findById(number));
    }

    @DeleteMapping("student/{number}")
    public ResponseEntity<?> deleteStudent(@PathVariable String number) {
        studentRepository.deleteById(number);
        return ResponseEntity.ok().build();
    }
}
