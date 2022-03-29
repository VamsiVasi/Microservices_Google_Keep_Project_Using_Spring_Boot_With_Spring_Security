package com.albanero.User_Microservice_.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class Global_ExceptionHandler_ {
    @ExceptionHandler(Resource_NotFound_Exception_.class)
    public ResponseEntity<?> resourceNotFoundException(Resource_NotFound_Exception_ ex, WebRequest request) {
    Error_Response_ errorDetails = new Error_Response_(ex.getMessage(),HttpStatus.NOT_FOUND.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExcpetionHandler(Exception ex, WebRequest request) {
    	Error_Response_ errorDetails = new Error_Response_(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}