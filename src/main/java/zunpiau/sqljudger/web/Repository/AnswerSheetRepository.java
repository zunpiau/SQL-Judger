package zunpiau.sqljudger.web.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.domain.AnswerSheet;

import java.util.List;

public interface AnswerSheetRepository extends CrudRepository<AnswerSheet, Long>, EnhanceRepository<AnswerSheet, Long> {

    boolean existsByExamAndStudent(Long exam, Long student);

    List<AnswerSheet> findAllByExam(Long exam);

    @Modifying
    @Transactional
    @Query("update AnswerSheet a set a.score = ?2 where a.id = ?1")
    int setScore(Long id, Integer score);

}
