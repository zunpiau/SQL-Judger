package zunpiau.sqljudger.web.Repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.domain.Exam;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends CrudRepository<Exam, Long>, EnhanceRepository<Exam, Long> {

    List<Exam> findAllByTeaching_Teacher_Number(Long teacher);

    List<Exam> findAllByTeaching_Id(Long teaching);

    Optional<Exam> findByIdAndTeaching_Teacher_Number(Long id, Long teacher);

    boolean existsByTestPaper_Id(Long testpaper);

    @Override
    @Cacheable(cacheNames = "Exam", key = "#id")
    Optional<Exam> findById(Long id);

    @Modifying
    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    @Query("update Exam e set e.status = ?2 where e.id = ?1")
    int setStatus(Long id, Integer status);

}
