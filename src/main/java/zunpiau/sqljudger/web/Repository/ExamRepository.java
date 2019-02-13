package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Exam;
import zunpiau.sqljudger.web.domain.Teacher;

import java.util.List;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long>, EnhanceRepository<Exam, Long> {

    Exam findByIdAndTeacher(Long id, Teacher teacher);

    List<Exam> findByTeacher(Teacher teacher);

}
