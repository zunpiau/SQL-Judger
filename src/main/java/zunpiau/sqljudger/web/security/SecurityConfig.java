package zunpiau.sqljudger.web.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }

    @Bean
    public JWTVerifier jwtVerifier(Algorithm algorithm) {
        return JWT.require(algorithm)
                .ignoreIssuedAt()
                .build();
    }

    @Configuration
    public static class WebMvcConfig implements WebMvcConfigurer {

        private final JWTVerifier jwtVerifier;
        @Value("${admin.name}")
        private String adminName;
        @Value("${admin.password}")
        private String adminPassword;

        @Autowired
        public WebMvcConfig(JWTVerifier jwtVerifier) {
            this.jwtVerifier = jwtVerifier;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new HttpBasicInterceptor(adminName, adminPassword))
                    .addPathPatterns("/admin/**");
            registry.addInterceptor(new JwtInterceptor(jwtVerifier, "student"))
                    .addPathPatterns("/student/**");
            registry.addInterceptor(new JwtInterceptor(jwtVerifier, "teacher"))
                    .addPathPatterns("/teacher/**");
            registry.addInterceptor(new JwtInterceptor(jwtVerifier, "student"))
                    .addPathPatterns("/student/**");
        }
    }
}
