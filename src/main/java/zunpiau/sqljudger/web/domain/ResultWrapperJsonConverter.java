package zunpiau.sqljudger.web.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import zunpiau.sqljudger.database.entity.ResultWrapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;

@Slf4j
public class ResultWrapperJsonConverter implements AttributeConverter<ResultWrapper, String> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(ResultWrapper attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.error("{}", attribute, e);
            throw new RuntimeException("Could not convert to Json", e);
        }
    }

    @Override
    public ResultWrapper convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return mapper.readValue(dbData, ResultWrapper.class);
        } catch (IOException e) {
            log.error("{}", dbData, e);
            throw new RuntimeException("Could not convert to ResultWrapper", e);
        }
    }
}
