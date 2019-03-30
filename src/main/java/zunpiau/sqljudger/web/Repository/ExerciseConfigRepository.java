package zunpiau.sqljudger.web.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.domain.ExerciseConfig;

import java.util.List;

public interface ExerciseConfigRepository extends CrudRepository<ExerciseConfig, Long> {

    @Transactional
    @Modifying
    @Query("delete from ExerciseConfig e where e.id in ?1")
    void deleteByIds(List<Long> ids);
}
