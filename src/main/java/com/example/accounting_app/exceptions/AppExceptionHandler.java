package com.example.accounting_app.exceptions;

import com.example.accounting_app.ui.response.CustomErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {UserServiceExceptions.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceExceptions ex, WebRequest request){

        CustomErrorMessage errorMessage = new CustomErrorMessage(new Date(), ex.getMessage());

        return new ResponseEntity<>(errorMessage ,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleOtherExceptions(Exception ex, WebRequest request){

        CustomErrorMessage errorMessage = new CustomErrorMessage(new Date(), ex.getMessage());

        return new ResponseEntity<>(errorMessage ,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
