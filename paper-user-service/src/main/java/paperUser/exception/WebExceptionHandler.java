package paperUser.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import paperUser.vo.Result;

@ControllerAdvice
public class WebExceptionHandler {
    @ExceptionHandler(value = {WebException.class})
    @ResponseBody
    public Result handle(WebException e) {
        return new Result(e.getHttpStatus().value(), e.getMessage());
    }
}
