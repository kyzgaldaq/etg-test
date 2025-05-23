package gts.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e,
                                                                      HttpServletRequest request) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null
                ? "Validation failed for field '" + fieldError.getField() + "': " + fieldError.getDefaultMessage()
                : "Validation error";
        return errorResponse(HttpStatus.BAD_REQUEST, e, request);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest request) {
        return errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,e, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e, HttpServletRequest request) {
        return errorResponse(HttpStatus.BAD_REQUEST, e, request);
    }

    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomNotFoundException e, HttpServletRequest request) {
        return errorResponse(HttpStatus.NOT_FOUND, e, request);
    }

    @ExceptionHandler(CustomAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomAlreadyExistsException e, HttpServletRequest request) {
        return errorResponse(HttpStatus.CONFLICT, e, request);
    }

    private static ResponseEntity<ErrorResponse> errorResponse(HttpStatus httpStatus, Exception e,  HttpServletRequest request){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(e.getMessage())
                .status(httpStatus.value())
                .path(request.getRequestURI())
                .error(httpStatus.getReasonPhrase())
                .build();

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
