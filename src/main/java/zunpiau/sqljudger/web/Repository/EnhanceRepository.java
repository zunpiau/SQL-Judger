package zunpiau.sqljudger.web.Repository;

import org.springframework.transaction.annotation.Transactional;

public interface EnhanceRepository<T, ID> {

    @Transactional
    T saveAndFresh(T t);

}
