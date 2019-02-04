package zunpiau.sqljudger.web.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import zunpiau.sqljudger.web.domain.Teaching;

@Repository
public interface TeachingRepository extends CrudRepository<Teaching, Long>, EnhanceRepository<Teaching, Long> {

}
