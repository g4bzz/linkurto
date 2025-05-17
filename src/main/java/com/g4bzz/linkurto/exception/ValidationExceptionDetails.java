package com.g4bzz.linkurto.exception;

import lombok.experimental.SuperBuilder;

import java.util.Map;

@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {
    Map<String,String> fields;
}
