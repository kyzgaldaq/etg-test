package gts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class CustomAlreadyExistsException extends RuntimeException {
    public CustomAlreadyExistsException(String message) {
        super(message);
    }
}
