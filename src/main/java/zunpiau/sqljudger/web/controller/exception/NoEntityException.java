package zunpiau.sqljudger.web.controller.exception;

public class NoEntityException extends BaseException {

    public static final int STATUS_NO_TEACHER = 4041;
    public static final int STATUS_NO_STUDENT = 4042;
    public static final int STATUS_NO_EXAM = 4043;
    public static final int STATUS_NO_ANSWERSHEET = 4044;
    public static final int STATUS_NO_TEACHING = 4045;
    public static final int STATUS_NO_TESTPAPER = 4046;
    public static final int STATUS_NO_EXERCISE_CONFIG = 4047;
    public static final int STATUS_NO_EXERCISE = 4048;
    private static final long serialVersionUID = -9052795832928097201L;

    public NoEntityException(int status) {
        super(status);
    }
}
