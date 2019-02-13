package zunpiau.sqljudger.web.Repository;

import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Exam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class WithDetachedReposityImpl implements WithDetachedReposity<Exam, Long> {

    @PersistenceContext
    private final EntityManager entityManager;

    public WithDetachedReposityImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void refresh(Exam exam) {
        entityManager.refresh(exam);
    }
//
//    @Override
//    @Transactional
//    public Exam saveAndfresh(Exam exam) {
////        final Set<Exercise> detachedExercises = exam.getExercises();
////        final Set<Exercise> exercises = new HashSet<>(detachedExercises.size(), 1);
////        for (Exercise detachedExercise : detachedExercises) {
////            final Exercise exercise = entityManager.find(Exercise.class, detachedExercise.getId());
////            exercises.add(exercise);
////        }
////        exam.setExercises(exercises);
////        entityManager.persist(exam);
//        save(exam);
//        return entityManager.find(Exam.class, exam.getId());
//    }
}
