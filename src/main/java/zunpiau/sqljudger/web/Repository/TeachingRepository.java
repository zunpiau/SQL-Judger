package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Teacher;
import zunpiau.sqljudger.web.domain.Teaching;

import java.util.List;

@Repository
public interface TeachingRepository extends CrudRepository<Teaching, Long>, EnhanceRepository<Teaching, Long> {

    List<Teaching> findTeachingsByTeacher(Teacher teacher);

}
