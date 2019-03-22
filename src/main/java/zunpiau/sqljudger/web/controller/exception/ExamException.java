package zunpiau.sqljudger.web.controller.exception;

public class ExamException extends BaseException {

    public static final int STATUS_NON_CORRECT = 4006;
    public static final int STATUS_ANSWERED = 4005;
    public static final int STATUS_NON_START = 4004;
    public static final int STATUS_FINISHED = 4003;
    public static final int STATUS_CANCELED = 4002;

    private static final long serialVersionUID = -6283524098180220480L;

    public ExamException(int status) {
        super(status);
    }

}
