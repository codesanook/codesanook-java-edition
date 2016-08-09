package com.codesanook.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no user with facebook id")
public class NoUserWithFcebookIdException extends ExceptionBase {

    private long facebookId;

    public NoUserWithFcebookIdException(long facebookId) {
        this.facebookId = facebookId;
    }

    @Override
    public String getMessage() {
        return String.format("no user with facebook id %d", facebookId);
    }
}
