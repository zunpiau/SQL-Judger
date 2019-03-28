package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Student;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long>, EnhanceRepository<Student, Long> {

    List<Student> findAllByClazz_Id(Long clazz);

}
