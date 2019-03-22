package zunpiau.sqljudger.web.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zunpiau.sqljudger.web.BaseResponse;
import zunpiau.sqljudger.web.controller.exception.BaseException;

@RestControllerAdvice
public class GloableExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public BaseResponse<?> handleExamException(BaseException e) {
        return new BaseResponse<>(e.getStatus());
    }

}
