package com.g4bzz.linkurto.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class EngineTypeExceptionDetails extends ExceptionDetails {
    private String requiredType;
    private String propertyName;
}
