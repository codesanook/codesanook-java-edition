package com.codesanook.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "NoUserWithIdException")
public class NoUserWithIdException extends ExceptionBase {

    private int userId;

    public NoUserWithIdException(int userId) {
        this.userId = userId;
    }

    @Override
    public String getMessage() {
        return String.format("no user with id %d", userId);
    }
}
