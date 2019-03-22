package zunpiau.sqljudger.web.security;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class JwtInterceptor extends HandlerInterceptorAdapter {

    public static final String ATTR_NUMBER = "attr_number";
    public static final String REDIRECT_FROM = "from";
    public static final String JWT_CLAIM_NUMBER = "number";
    private final JWTVerifier jwtVerifier;
    private final String role;

    public JwtInterceptor(JWTVerifier jwtVerifier, String role) {
        this.jwtVerifier = jwtVerifier;
        this.role = role;
    }

    @Nullable
    private static String getCookieValue(String name, HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String token = getCookieValue(role, request);
        if (token != null) {
            try {
                final DecodedJWT jwt = jwtVerifier.verify(token);
                request.setAttribute(ATTR_NUMBER, jwt.getClaim(JWT_CLAIM_NUMBER).asLong());
                return super.preHandle(request, response, handler);
            } catch (JWTVerificationException ignore) {
            }
        }
        final String requestURI = request.getRequestURI();
        if (requestURI.endsWith(".html")) {
            final String from = URLEncoder.encode(requestURI + '?' + request.getQueryString(),
                    StandardCharsets.UTF_8.name());
            final String location = new StringBuilder().append("/view/login.html?")
                    .append(REDIRECT_FROM)
                    .append('=')
                    .append(from)
                    .toString();
            response.sendRedirect(location);
            return false;
        }
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
