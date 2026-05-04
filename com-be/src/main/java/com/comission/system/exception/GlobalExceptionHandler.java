package com.comission.system.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        ErrorResponse response = new ErrorResponse();
        response.setStatus(400);
        response.setCode(ex.getErrorCode().getErrorCode());
        response.setMessage(ex.getErrorCode().getErrorMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        ErrorResponse response = new ErrorResponse();
        response.setStatus(400);
        response.setCode("VALIDATION_ERROR");
        response.setMessage("Validation failed");
        response.setErrors(errors);
        return ResponseEntity.badRequest().body(response);
    }
}
