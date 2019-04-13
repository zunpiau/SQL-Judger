package zunpiau.sqljudger.web.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import zunpiau.sqljudger.web.Repository.StudentRepository;
import zunpiau.sqljudger.web.domain.Student;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Cacheable(cacheNames = "student", key = "#id")
    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    List<Student> findAllByClazz_Id(Long clazz) {
        return studentRepository.findAllByClazz_Id(clazz);
    }
}
