package zunpiau.sqljudger.web.Repository;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EnhanceRepositoryImpl<T, ID> implements EnhanceRepository<T, ID> {

    @PersistenceContext
    private final EntityManager entityManager;

    public EnhanceRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public T saveAndFresh(T t) {
        entityManager.persist(t);
        entityManager.refresh(t);
        return t;
    }
}
