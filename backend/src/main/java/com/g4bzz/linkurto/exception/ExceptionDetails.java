package com.g4bzz.linkurto.exception;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class ExceptionDetails {
    private String title;
    private int status;
    private String developerMessage;
    //private String details;
    private LocalDateTime timestamp;
}
