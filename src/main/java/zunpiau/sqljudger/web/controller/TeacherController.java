package zunpiau.sqljudger.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zunpiau.sqljudger.web.Repository.ExerciseRepository;
import zunpiau.sqljudger.web.Repository.TeacherRepository;

@RestController
@RequestMapping("/teacher/")
public class TeacherController {

    private final ExerciseRepository exerciseRepository;
    private final TeacherRepository teacherRepository;

    @Autowired
    public TeacherController(ExerciseRepository exerciseRepository,
            TeacherRepository teacherRepository) {
        this.exerciseRepository = exerciseRepository;
        this.teacherRepository = teacherRepository;
    }

    @GetMapping("exercise")
    public ResponseEntity<?> addExercise() {
        return ResponseEntity.ok(exerciseRepository.findAll());
    }


}
