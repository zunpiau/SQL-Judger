package zunpiau.sqljudger.web.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
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
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (role.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    if (token != null) {
                        try {
                            jwtVerifier.verify(token);
                            return super.preHandle(request, response, handler);
                        } catch (JWTVerificationException ignore) {
                        }
                    }
                    break;
                }
            }
        }
        if (request.getRequestURI().endsWith(".html")) {
            response.sendRedirect("/view/login.html");
            return false;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
