package zunpiau.sqljudger.web.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.domain.AnswerSheet;

import java.util.List;

public interface AnswerSheetRepository extends CrudRepository<AnswerSheet, Long>, EnhanceRepository<AnswerSheet, Long> {

    boolean existsByExamAndStudent(Long exam, Long student);

    AnswerSheet findByExamAndStudent(Long exam, Long student);

    @Query("select a.id from AnswerSheet a where a.id = ?1 and a.student = ?2")
    Long getIdByExamAndStudent(Long exam, Long student);

    @Query("select new AnswerSheet(a.id, a.score) from AnswerSheet a where a.exam = ?1 and a.student = ?2")
    AnswerSheet getScore(Long exam, Long student);

    List<AnswerSheet> findAllByExam(Long exam);

    @Modifying
    @Transactional
    @Query("update AnswerSheet a set a.score = ?2 where a.id = ?1")
    int setScore(Long id, Integer score);

}
