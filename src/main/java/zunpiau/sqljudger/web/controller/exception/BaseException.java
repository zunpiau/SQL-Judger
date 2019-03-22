package zunpiau.sqljudger.web.controller.exception;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 843987388386902535L;
    protected final int status;

    public BaseException(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
