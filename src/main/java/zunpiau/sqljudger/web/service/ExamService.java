package zunpiau.sqljudger.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.Repository.ExamRepository;
import zunpiau.sqljudger.web.Repository.ExerciseRepository;
import zunpiau.sqljudger.web.domain.Exam;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.Teacher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExamService(ExamRepository examRepository,
            ExerciseRepository exerciseRepository) {
        this.examRepository = examRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public Exam save(Exam exam) {
        final Set<Exercise> detachedExercises = exam.getExercises();
        final Set<Exercise> exercises = new HashSet<>(detachedExercises.size(), 1);
        for (Exercise detachedExercise : detachedExercises) {
            final Exercise exercise = exerciseRepository.findById(detachedExercise.getId()).get();
            exercises.add(exercise);
        }
        exam.setExercises(exercises);
        examRepository.save(exam);
        return exam;
    }

    public Exam findByIdAndTeacher(Long id, Long teacher) {
        return examRepository.findByIdAndTeacher(id, new Teacher(teacher));
    }

    public List<Exam> findByTeacher(Long teacher) {
        return examRepository.findByTeacher(new Teacher(teacher));
    }

    @Transactional
    public boolean cancelExam(Long examId, Long teacherNumber) {
        final Exam exam = findByIdAndTeacher(examId, teacherNumber);
        if (exam != null) {
            exam.setCancel(true);
            examRepository.merger(exam);
            return true;
        }
        return false;
    }

}
