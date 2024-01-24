package com.firestms.Exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handlerResourceNotFoundExcpetion(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
            new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value= {ConflictException.class})
    protected ResponseEntity<Object> handlerConflictException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
            new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value= {AssignmentException.class})
    protected ResponseEntity<Object> handlerAssignmentException(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    //TODO
//    @ExceptionHandler(value= {DateTimeParseException.class})
//    protected ResponseEntity<Object> handlerDataTimeParceException(RuntimeException ex, WebRequest request){
//        String bodyOfResponse = "problem z formatem";
//        return handleExceptionInternal(ex, bodyOfResponse,
//            new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
//    }
}
