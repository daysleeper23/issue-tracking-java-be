package org.projectmanagement.application.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public ApplicationException(AppMessage from){
        this.status = HttpStatus.BAD_REQUEST;
        this.message = from.getMessage();
    }

    public ApplicationException(HttpStatus status,AppMessage from){
        this.status = status;
        this.message = from.getMessage();
    }

    public ApplicationException(String message){
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
    }

}
