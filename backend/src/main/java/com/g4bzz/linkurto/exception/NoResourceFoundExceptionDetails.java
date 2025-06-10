package com.g4bzz.linkurto.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class NoResourceFoundExceptionDetails extends ExceptionDetails {
    private String resourcePath;
}
