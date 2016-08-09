package com.codesanook.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
        reason = "validation error")  // 404
public class ValidationException extends ExceptionBase {


    @Override
    public String getMessage() {
        return "validation error";
    }
}
