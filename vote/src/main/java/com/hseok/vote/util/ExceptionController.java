package com.hseok.vote.util;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException exception) {
        System.out.println("Handling IllegalArgumentException: " + exception.getMessage());
        return ResponseHandler.responseBuilder(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                null);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception) {
        System.out.println("Handling EntityNotFoundException: " + exception.getMessage());
        return ResponseHandler.responseBuilder(
                HttpStatus.NOT_FOUND,
                exception.getMessage(),
                null
        );
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(NoResourceFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseHandler.responseBuilder(
                HttpStatus.NOT_FOUND,
                "해당하는 리소스를 찾지 못했어요.",
                null
        );
    }
}
