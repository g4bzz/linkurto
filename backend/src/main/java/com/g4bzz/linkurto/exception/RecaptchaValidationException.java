package com.g4bzz.linkurto.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RecaptchaValidationException extends RuntimeException {
    @Getter
    private final List<String> errorCodes;
    public RecaptchaValidationException(String message, List<String> errorCodes) {
        super(message);
        this.errorCodes = errorCodes;
    }
}
