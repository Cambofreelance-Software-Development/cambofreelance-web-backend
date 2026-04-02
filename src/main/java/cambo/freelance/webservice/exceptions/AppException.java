package cambo.freelance.webservice.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Setter
@Getter
public class AppException extends Exception{
    private int code;
    private String message;
    private HttpStatus httpStatus;
    private Map<String, String> data;

    public AppException() {
        super();
    }

    public AppException(int code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }


    public AppException(int code, String message, HttpStatus httpStatus, Map<String, String> data) {
        super(message);
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
        this.data = data;
    }
}