package sit.int221.bookingproj.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errorMaping = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorMaping.put(error.getField(), error.getDefaultMessage());
        });

//        ex.getBindingResult().getFieldError().getDefaultMessage();
        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Validation Error", errorMaping);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(IllegalStateException.class)
//    protected ResponseEntity<Object> handleIllegalStateNotValid(IllegalStateException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        Map<String, String> errorMaping = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error -> {
//            errorMaping.put(error.getField(), error.getDefaultMessage());
//        });
//        ErrorModel error = new ErrorModel(HttpStatus.BAD_REQUEST, "Invalid Form", ex.getMessage());
//
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(EntityNotFoundException.class)
//    private ResponseEntity<ErrorModel> handleEntityNotFound(EntityNotFoundException ex){
//        ErrorModel error = new ErrorModel(HttpStatus.NOT_FOUND, "Entity not found", ex.getMessage());
//
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
}