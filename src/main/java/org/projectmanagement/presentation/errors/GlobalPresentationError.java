package org.projectmanagement.presentation.errors;

import org.projectmanagement.domain.exceptions.ResourceNotFoundException;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(ResourceNotFoundException ex) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse(404, errors), null, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(MethodArgumentTypeMismatchException ex) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse(400, errors), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<GlobalResponse> handleApplicationException(ApplicationException ex){
        List<GlobalResponse.ErrorItem> errorItems = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse(ex.getStatus().value(),errorItems),null,ex.getStatus());
    }

}
