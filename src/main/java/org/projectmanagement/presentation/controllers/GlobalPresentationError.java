package org.projectmanagement.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalPresentationError {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<GlobalResponse.ErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new GlobalResponse.ErrorItem(err.getField() + " " + err.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(new GlobalResponse(400, errors), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(HttpMessageNotReadableException ex) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem("Invalid JSON"));
        return new ResponseEntity<>(new GlobalResponse(400, errors), null, HttpStatus.BAD_REQUEST);
    }


}
