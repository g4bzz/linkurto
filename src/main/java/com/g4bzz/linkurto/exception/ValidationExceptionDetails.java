package com.g4bzz.linkurto.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails {
    Map<String,String> fields;
}
