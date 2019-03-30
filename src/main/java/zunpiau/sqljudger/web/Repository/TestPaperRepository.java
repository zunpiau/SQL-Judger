package zunpiau.sqljudger.web.Repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.domain.TestPaper;

import java.util.List;
import java.util.Optional;

public interface TestPaperRepository extends CrudRepository<TestPaper, Long> {

    @Modifying
    @Transactional
    int deleteByIdAndTeacher_Number(Long id, Long number);

    Optional<TestPaper> findByIdAndTeacher_Number(Long id, Long number);

    @Override
    List<TestPaper> findAll();

}
