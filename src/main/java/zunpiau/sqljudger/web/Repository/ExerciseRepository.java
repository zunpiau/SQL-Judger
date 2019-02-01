package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Exercise;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {

}
