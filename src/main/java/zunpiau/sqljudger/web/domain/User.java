package zunpiau.sqljudger.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number;
    @Column(length = 16, nullable = false)
    private String name;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(length = 60, nullable = false)
    private String password;

    protected User(Long number) {
        this.number = number;
    }

}
