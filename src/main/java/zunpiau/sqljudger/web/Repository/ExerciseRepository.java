package zunpiau.sqljudger.web.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.Teacher;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {

    @Modifying
    @Transactional
    int deleteByIdAndAndTeacher(Long id, Teacher teacher);
}
