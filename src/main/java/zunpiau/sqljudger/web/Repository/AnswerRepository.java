package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import zunpiau.sqljudger.web.domain.Answer;

import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer, Long>, EnhanceRepository<Answer, Long> {

    List<Answer> findAllByAnswerSheet(Long answerSheet);
}
