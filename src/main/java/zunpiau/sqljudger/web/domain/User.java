package zunpiau.sqljudger.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public abstract class User {

    @Id
    @Column(length = 10, nullable = false)
    private String number;
    @Column(length = 16, nullable = false)
    private String name;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 60, nullable = false)
    private String password;

}
