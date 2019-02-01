package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Student;

@Repository
public interface StudentRepository extends CrudRepository<Student, String> {

}
