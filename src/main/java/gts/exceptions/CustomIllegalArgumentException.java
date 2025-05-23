package gts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CustomIllegalArgumentException extends RuntimeException{
    public CustomIllegalArgumentException(String message) {
        super(message);
    }
}
