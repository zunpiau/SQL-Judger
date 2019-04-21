package zunpiau.sqljudger.web.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.ExerciseType;
import zunpiau.sqljudger.web.domain.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long>, EnhanceRepository<Exercise, Long> {

    @Modifying
    @Transactional
    int deleteByIdAndTeacher(Long id, Teacher teacher);

    List<Exercise> findAllByIdIn(Iterable<Long> ids);

    @Query("select new Exercise(e.id, e.title, e.description, e.score, e.inputSQL, e.inputData) " +
           " from Exercise e where e.id = ?1")
    Optional<Exercise> findByIdForStudent(Long id);

    @Query("select new Exercise(e.id, e.inputSQL, e.score, e.expectedData) " +
           " from Exercise e where e.id = ?1")
    Optional<Exercise> findByIdForAnswer(Long id);

    List<Exercise> findAllByType(ExerciseType type);

}
