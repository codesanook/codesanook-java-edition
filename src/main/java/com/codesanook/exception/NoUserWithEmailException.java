package com.codesanook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="no user with given email")
public class NoUserWithEmailException  extends ExceptionBase {

    private String email;

    public NoUserWithEmailException(String email) {
        this.email = email;
    }

    @Override
    public String getMessage() {
        return  String.format("no user with email %s",email);
    }
}
