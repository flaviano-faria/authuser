package com.ead.authuser.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorRecordResponse> handleNotFoundException(NotFoundException e) {
        ErrorRecordResponse errorResponse = new ErrorRecordResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        logger.error("handleNotFoundException message: {}", e.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedUsernameException.class)
    public ResponseEntity<ErrorRecordResponse> handleDuplicatedUsernameException(DuplicatedUsernameException e) {
        ErrorRecordResponse errorResponse = new ErrorRecordResponse(
                HttpStatus.CONFLICT.value(), e.getMessage(), null);
        logger.error("handleDuplicatedUsernameException message: {}", e.getMessage());
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRecordResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
           String fieldName  = ((FieldError) error).getField();
           String errorMessage = error.getDefaultMessage();
           errors.put(fieldName, errorMessage);
        });
        logger.error("handleMethodArgumentNotValidException message: {}", e.getMessage());
        var  errorResponse = new ErrorRecordResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Error: Validation failed", errors);
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

}
