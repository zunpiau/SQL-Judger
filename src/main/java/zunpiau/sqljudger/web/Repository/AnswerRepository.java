package zunpiau.sqljudger.web.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.domain.Answer;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer, Long>, EnhanceRepository<Answer, Long> {

    List<Answer> findAllByAnswerSheet(Long answerSheet);

    Answer findByExerciseConfigAndAnswerSheet(Long exerciseConfig, Long answerSheet);

    @Modifying
    @Transactional
    @Query("update Answer a set a.score = ?2 where a.id = ?1")
    int updateScore(Long id, Integer score);
}
