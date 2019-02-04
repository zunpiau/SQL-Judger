package zunpiau.sqljudger.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exam {

    private Clazz clazz;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
