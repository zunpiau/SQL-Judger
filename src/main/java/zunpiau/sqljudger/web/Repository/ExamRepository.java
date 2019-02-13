package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Exam;

import java.util.List;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long>, EnhanceRepository<Exam, Long> {

    Exam findByIdAndTeaching_Teacher_Number(Long id, Long number);

    List<Exam> findByTeaching_Teacher_Number(Long number);

}
