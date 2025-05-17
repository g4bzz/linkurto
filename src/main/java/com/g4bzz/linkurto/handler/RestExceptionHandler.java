package com.g4bzz.linkurto.handler;

import com.g4bzz.linkurto.exception.BadRequestException;
import com.g4bzz.linkurto.exception.BadRequestExceptionDetails;
import com.g4bzz.linkurto.exception.EngineTypeExceptionDetails;
import com.g4bzz.linkurto.exception.ValidationExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler{

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException exception) {
        return new ResponseEntity<>(BadRequestExceptionDetails.builder()
                .title("Bad Request Exception: Check the documentation")
                .status(HttpStatus.BAD_REQUEST.value())
                //.details(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .developerMessage(exception.getClass().getName())
                .build(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException exception) {
        return new ResponseEntity<>(EngineTypeExceptionDetails.builder()
                .title("Bad Request Exception: Check the documentation")
                .status(HttpStatus.BAD_REQUEST.value())
                //.details(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .developerMessage(exception.getClass().getName())
                .requiredType(exception.getRequiredType() != null ? exception.getRequiredType().getName() : "")
                .propertyName(exception.getPropertyName())
                .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String, String> fieldErrors = exception.getFieldErrors().stream().collect(
            Collectors.toMap(
                FieldError::getField,
                fieldError -> (fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "")
            )
        );

        return new ResponseEntity<>(ValidationExceptionDetails.builder()
                .title("Bad Request Exception: Check the documentation")
                .status(HttpStatus.BAD_REQUEST.value())
                //.details(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .developerMessage(exception.getClass().getName())
                .fields(fieldErrors)
                .build(), HttpStatus.BAD_REQUEST);
    }

}
