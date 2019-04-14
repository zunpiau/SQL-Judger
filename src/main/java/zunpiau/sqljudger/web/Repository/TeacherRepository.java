package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Teacher;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Long>, EnhanceRepository<Teacher, Long> {

}
