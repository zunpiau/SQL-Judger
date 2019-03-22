package zunpiau.sqljudger.web.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Teacher;
import zunpiau.sqljudger.web.domain.Teaching;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeachingRepository extends CrudRepository<Teaching, Long>, EnhanceRepository<Teaching, Long> {

    @Query("select t.id from Teaching t where t.teacher = ?1")
    List<Long> findTeachingIdsByTeacher(Teacher teacher);

    List<Teaching> findAllByTeacher_Number(Long teacher);

    Optional<Teaching> findByClazz_IdAndTeacher_Number(Long clazz, Long teacher);

    Teaching findByClazz_Id(Long clazz);

}
