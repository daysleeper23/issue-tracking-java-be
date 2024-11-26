package org.projectmanagement.presentation.errors;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.projectmanagement.domain.exceptions.*;
import org.projectmanagement.application.exceptions.ApplicationException;
import org.projectmanagement.presentation.response.GlobalResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalPresentationError {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        List<GlobalResponse.ErrorItem> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new GlobalResponse.ErrorItem(err.getField() + " " + err.getDefaultMessage()))
                .toList();
        return new ResponseEntity<>(new GlobalResponse(400, errors), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<GlobalResponse> handleValidationExceptions(InvalidInputException ex) {

        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
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

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<GlobalResponse> handleAResourceAlreadyExistsException(ResourceAlreadyExistsException ex){
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse(409 ,errors),null,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(SystemRoleUpdateException.class)
    public ResponseEntity<GlobalResponse> handleSystemRoleUpdateException(SystemRoleUpdateException ex) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse(403, errors), null, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GlobalResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = CustomDataIntegrityViolationMessage.getMessage(ex.getCause());
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem("Duplicate resource: " + message));
        return new ResponseEntity<>(new GlobalResponse(409, errors), null, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<GlobalResponse> handleDuplicateResourceException(DuplicateResourceException ex) {
        List<GlobalResponse.ErrorItem> errors = List.of(new GlobalResponse.ErrorItem(ex.getMessage()));
        return new ResponseEntity<>(new GlobalResponse(409, errors), null, HttpStatus.CONFLICT);
    }

}
