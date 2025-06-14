package com.g4bzz.linkurto.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Getter
public class RecaptchaValidationExceptionDetails extends ExceptionDetails {
    private List<String> errorMessages;
}
