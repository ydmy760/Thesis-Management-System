package paperUser.exception;

import org.springframework.http.HttpStatus;

public class WebException extends RuntimeException {
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public WebException(String message) {
        super(message);
    }

    public WebException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
