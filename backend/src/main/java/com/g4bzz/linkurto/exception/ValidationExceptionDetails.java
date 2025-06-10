package com.g4bzz.linkurto.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
@Getter
public class ValidationExceptionDetails extends ExceptionDetails {
    private Map<String,String> fields;
}
