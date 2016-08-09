package com.codesanook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED,
        reason = "UnauthorizedException")  // 404
public class UnauthorizedException extends RuntimeException {

    private String message;

    public UnauthorizedException() {
        message = "user hasn't log in yet";
    }

    public UnauthorizedException(String message) {
        this.message = message;
    }


    @Override
    public String getMessage() {
        return message;

    }
}
