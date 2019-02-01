package zunpiau.sqljudger.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zunpiau.sqljudger.web.Repository.ExerciseRepository;
import zunpiau.sqljudger.web.Repository.TeacherRepository;
import zunpiau.sqljudger.web.domain.Exercise;

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

    @GetMapping(value = "exercise", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addExercise(@RequestBody Exercise exercise) {
        exerciseRepository.save(exercise);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{number}")
    public ResponseEntity<?> getTeacher(@PathVariable String number) {
        return ResponseEntity.ok(teacherRepository.findById(number));
    }


}
