package zunpiau.sqljudger.web.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import zunpiau.sqljudger.database.entity.ResultWrapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;

public class ResultWrapperJsonConverter implements AttributeConverter<ResultWrapper, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ResultWrapper attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Could not convert to Json", e);
        }
    }

    @Override
    public ResultWrapper convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, ResultWrapper.class);
        } catch (IOException e) {
            throw new RuntimeException("Could not convert to ResultWrapper", e);
        }
    }
}
