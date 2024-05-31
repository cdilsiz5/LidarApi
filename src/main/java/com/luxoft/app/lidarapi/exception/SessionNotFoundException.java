package com.luxoft.app.lidarapi.exception;

import org.springframework.http.HttpStatus;

public class SessionNotFoundException extends ApiException {
    public SessionNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
