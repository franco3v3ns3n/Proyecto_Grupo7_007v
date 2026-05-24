package com.hotel.msauth.exceptions;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class RemoteServiceException extends RuntimeException {
    private final HttpStatus status;

    public RemoteServiceException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
