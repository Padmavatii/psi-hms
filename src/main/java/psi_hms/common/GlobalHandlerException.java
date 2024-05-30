package psi_hms.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalHandlerException {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<Object> genericException(GenericException ex){
        Map<String, String> errors = new HashMap<>();
        errors.put("status",String.valueOf(ex.getStatusCode()));
        errors.put("errorResponse",ex.getErrorMessage());
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST) ;
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> NotFoundException(NotFoundException ex) {
        String message = ex.getMessage();
        return new ResponseEntity<Object>(message, HttpStatus.BAD_REQUEST);
    }
}
