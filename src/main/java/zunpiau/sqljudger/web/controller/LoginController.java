package zunpiau.sqljudger.web.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import zunpiau.sqljudger.web.BaseResponse;
import zunpiau.sqljudger.web.Repository.StudentRepository;
import zunpiau.sqljudger.web.Repository.TeacherRepository;
import zunpiau.sqljudger.web.domain.User;
import zunpiau.sqljudger.web.security.JwtInterceptor;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;


@Controller
public class LoginController {

    private final PasswordEncoder encoder;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final Algorithm algorithm;
    @Value("${admin.name}")
    private String adminName;
    @Value("{admin.password}")
    private String adminPassword;
    @Value("${jwt.expiration}")
    private long expiration;

    @Autowired
    public LoginController(PasswordEncoder encoder,
            TeacherRepository teacherRepository, StudentRepository studentRepository,
            Algorithm algorithm) {
        this.encoder = encoder;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.algorithm = algorithm;
    }


    private static Date createDateAfter(long secondsToAdd) {
        return Date.from(Instant.now().plus(secondsToAdd, ChronoUnit.SECONDS));
    }


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> login(Long number, String password, String type,
            @RequestParam(name = JwtInterceptor.REDIRECT_FROM, required = false) String from) throws Exception {
        if (StringUtils.isEmpty(number) || StringUtils.isEmpty(password)) {
            return ResponseEntity.badRequest().build();
        }
        Optional<? extends User> optionalUser;
        String role;
        String location;
        if ("teacher".equals(type)) {
            optionalUser = teacherRepository.findById(number);
            role = "teacher";
            location = "/view/teacher.html";
        } else {
            optionalUser = studentRepository.findById(number);
            role = "student";
            location = "/view/student.html";
        }
        String redirect = StringUtils.isEmpty(from) ? location : from;
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (encoder.matches(password, user.getPassword())) {
                String token = JWT.create()
                        .withClaim("number", user.getNumber())
                        .withClaim("name", user.getName())
                        .withExpiresAt(createDateAfter(expiration))
                        .sign(algorithm);
                String tokenCookie = ResponseCookie.from(role, token)
                        .maxAge(Duration.ofDays(1))
                        .httpOnly(true)
                        .build()
                        .toString();
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, tokenCookie)
                        .body(BaseResponse.ok(redirect));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
