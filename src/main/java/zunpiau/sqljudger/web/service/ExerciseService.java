package zunpiau.sqljudger.web.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zunpiau.sqljudger.web.Repository.ExerciseRepository;
import zunpiau.sqljudger.web.domain.Exercise;
import zunpiau.sqljudger.web.domain.Teacher;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final JdbcService jdbcService;

    public ExerciseService(ExerciseRepository exerciseRepository, JdbcService jdbcService) {
        this.exerciseRepository = exerciseRepository;
        this.jdbcService = jdbcService;
    }

    public Exercise saveOrUpdate(Exercise exercise) {
        if (exercise.getId() != null) {
            return exerciseRepository.merger(exercise);
        }
        return exerciseRepository.save(exercise);
    }

    public List<Exercise> convertExercise(MultipartFile file) throws IOException {
        Workbook workbook;
        workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        ArrayList<Exercise> exercises = new ArrayList<>();
        for (Row row : sheet) {
            Exercise exercise = new Exercise(row.getCell(0).getStringCellValue(),
                    row.getCell(1).getStringCellValue(),
                    (int) row.getCell(4).getNumericCellValue(),
                    row.getCell(2).getStringCellValue(),
                    row.getCell(3).getStringCellValue());
            exercises.add(exercise);
        }
        return exercises;
    }

    @Async("postExecutor")
    public void batchAdd(List<Exercise> exercises, Long number) {
        Teacher teacher = new Teacher(number);
        for (Exercise exercise : exercises) {
            exercise.setTeacher(teacher);
            try {
                exercise.setInputData(jdbcService.excuteAndRetrieve(exercise.getInputSQL()));
                exercise.setExpectedData(jdbcService.excute(exercise.getInputSQL(), exercise.getExpectedSQL(),
                        exercise.isRowOrder(), exercise.isColumnOrder()));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        exerciseRepository.saveAll(exercises);
    }

}
