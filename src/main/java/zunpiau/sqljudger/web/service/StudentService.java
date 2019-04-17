package zunpiau.sqljudger.web.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
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

    @CachePut(cacheNames = "student", key = "#student.getNumber()")
    public Student add(Student student) {
        return studentRepository.saveAndFresh(student);
    }

    @CachePut(cacheNames = "student", key = "#student.getNumber()")
    public Student update(Student student) {
        return studentRepository.merger(student);
    }

    @Cacheable(cacheNames = "student", key = "#number")
    public Student findById(Long number) {
        return studentRepository.findById(number).orElse(null);
    }

    public Iterable<Student> findAll() {
        return studentRepository.findAll();
    }

    @CacheEvict(cacheNames = "student", key = "#number")
    public void deleteById(Long number) {
        studentRepository.deleteById(number);
    }

    List<Student> findAllByClazz_Id(Long clazz) {
        return studentRepository.findAllByClazz_Id(clazz);
    }
}
