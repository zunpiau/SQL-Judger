package zunpiau.sqljudger.web.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import zunpiau.sqljudger.database.entity.ResultWrapper;
import zunpiau.sqljudger.database.entity.SimpleTable;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Exercise implements Serializable {

    private static final int TYPE_OUTPUT = 1;
    private static final int TYPE_SCHEMA = 2;
    private static final long serialVersionUID = -5806651385169097504L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512, nullable = false)
    private String title;

    @JoinColumn(name = "teacher_number")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Teacher teacher;

    @Column(length = 4096, nullable = false)
    private String description;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private ExerciseType type;

    private int score;

    @Column(length = 2048)
    private String inputSQL;

    @Convert(converter = SimpleTablesJsonConverter.class)
    @Column(length = 2048)
    private List<SimpleTable> inputData;

    @Column(length = 2048)
    private String expectedSQL;

    @Convert(converter = ResultWrapperJsonConverter.class)
    @Column(length = 2048, nullable = false)
    private ResultWrapper expectedData;

    public Exercise(Long id) {
        this.id = id;
    }

    public Exercise(Long id, String inputSQL, int score, ResultWrapper expectedData) {
        this.id = id;
        this.inputSQL = inputSQL;
        this.score = score;
        this.expectedData = expectedData;
    }

    public Exercise(Long id, String title, String description, int score, String inputSQL,
            List<SimpleTable> inputData) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.score = score;
        this.inputSQL = inputSQL;
        this.inputData = inputData;
    }

    public Exercise(String title, String description, int score, ResultWrapper expectedData) {
        this.title = title;
        this.description = description;
        this.score = score;
        this.expectedData = expectedData;
    }

    public Exercise(String title, String description, int score, String inputSQL, String expectedSQL) {
        this.title = title;
        this.description = description;
        this.score = score;
        this.inputSQL = inputSQL;
        this.expectedSQL = expectedSQL;
    }

    public static class SimpleTablesJsonConverter implements AttributeConverter<List<SimpleTable>, String> {

        private final ObjectMapper mapper = new ObjectMapper();
        private final CollectionType collectionType = mapper.getTypeFactory()
                .constructCollectionType(List.class, SimpleTable.class);

        @Override
        public String convertToDatabaseColumn(List<SimpleTable> attribute) {
            try {
                return mapper.writeValueAsString(attribute);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Could not convert to Json", e);
            }
        }

        @Override
        public List<SimpleTable> convertToEntityAttribute(String dbData) {
            try {
                return mapper.readValue(dbData, collectionType);
            } catch (IOException e) {
                throw new RuntimeException("Could not convert to ResultWrapper", e);
            }
        }
    }
}
