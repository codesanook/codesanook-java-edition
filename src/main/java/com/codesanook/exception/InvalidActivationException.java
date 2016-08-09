package com.codesanook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "NoUserWithIdException")
public class InvalidActivationException extends ExceptionBase {

    @Override
    public String getMessage() {
        return "You haven't activate your account or invalid activation code.";
    }
}
