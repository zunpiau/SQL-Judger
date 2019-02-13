package zunpiau.sqljudger.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zunpiau.sqljudger.web.Repository.ExamRepository;
import zunpiau.sqljudger.web.Repository.TeachingRepository;
import zunpiau.sqljudger.web.domain.Exam;

import java.util.List;

@Service
public class ExamService {

    private final ExamRepository examRepository;
    private final TeachingRepository teachingRepository;

    @Autowired
    public ExamService(ExamRepository examRepository,
            TeachingRepository teachingRepository) {
        this.examRepository = examRepository;
        this.teachingRepository = teachingRepository;
    }

    @Transactional
    public Exam save(Exam exam, Long teacher) {
        final Long clazz = exam.getTeaching().getClazz().getId();
        exam.setTeaching(teachingRepository.findByClazz_IdAndTeacher_Number(clazz, teacher));
        examRepository.save(exam);
        return exam;
    }

    public Exam findByIdAndTeacher(Long id, Long teacher) {
        return examRepository.findByIdAndTeaching_Teacher_Number(id, teacher);
    }

    public List<Exam> findByTeacher(Long teacher) {
        return examRepository.findByTeaching_Teacher_Number(teacher);
    }

    @Transactional
    public boolean cancelExam(Long examId, Long teacherNumber) {
        final Exam exam = findByIdAndTeacher(examId, teacherNumber);
        if (exam != null) {
            exam.setCancel(true);
            examRepository.merger(exam);
            return true;
        }
        return false;
    }

}
