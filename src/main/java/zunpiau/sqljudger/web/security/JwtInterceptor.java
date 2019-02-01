package zunpiau.sqljudger.web.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtInterceptor extends HandlerInterceptorAdapter {

    private final JWTVerifier jwtVerifier;
    private final String role;

    public JwtInterceptor(JWTVerifier jwtVerifier, String role) {
        this.jwtVerifier = jwtVerifier;
        this.role = role;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            try {
                DecodedJWT jwt = jwtVerifier.verify(token);
                String roleClaim = jwt.getClaim("role").asString();
                if (roleClaim.equals(role)) {
                    return super.preHandle(request, response, handler);
                }
            } catch (JWTVerificationException e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
            }
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
