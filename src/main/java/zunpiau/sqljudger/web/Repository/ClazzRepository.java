package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Clazz;

@Repository
public interface ClazzRepository extends CrudRepository<Clazz, Long> {

}
