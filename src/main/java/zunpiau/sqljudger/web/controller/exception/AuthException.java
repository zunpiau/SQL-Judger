package zunpiau.sqljudger.web.controller.exception;

public class AuthException extends BaseException {

    public static final int STATUS_TEACHER = 4010;
    public static final int STATUS_STUDENT = 4011;

    private static final long serialVersionUID = 4028228111066628608L;

    public AuthException(int status) {
        super(status);
    }

}
