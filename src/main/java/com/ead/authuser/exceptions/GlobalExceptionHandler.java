package com.ead.authuser.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorRecordResponse> handleNotFoundException(NotFoundException e) {
        ErrorRecordResponse errorResponse = new ErrorRecordResponse(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedUsernameException.class)
    public ResponseEntity<ErrorRecordResponse> handleDuplicatedUsernameException(DuplicatedUsernameException e) {
        ErrorRecordResponse errorResponse = new ErrorRecordResponse(
                HttpStatus.CONFLICT.value(), e.getMessage(), null);
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }


}
