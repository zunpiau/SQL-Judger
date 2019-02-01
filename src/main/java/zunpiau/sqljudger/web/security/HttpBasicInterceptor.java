package zunpiau.sqljudger.web.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpBasicInterceptor extends HandlerInterceptorAdapter {

    private final String auth;

    public HttpBasicInterceptor(String name, String password) {
        auth = "Basic " + Base64Utils.encodeToString((name + ':' + password).getBytes());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!auth.equals(authHeader)) {
            response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Admin Area\"");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
        return super.preHandle(request, response, handler);
    }
}
