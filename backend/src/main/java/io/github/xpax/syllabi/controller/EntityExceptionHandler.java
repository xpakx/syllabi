package io.github.xpax.syllabi.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.Dictionary;
import java.util.Hashtable;

@ControllerAdvice
public class EntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<Object> handleNoResultException(final EmptyResultDataAccessException exception,
                                                          final WebRequest request) {
        return handleExceptionInternal(exception, null, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleBadRequestException(final ValidationException exception,
                                                            final WebRequest request) {
        Dictionary<String, String> body = new Hashtable<>();
        body.put("message", exception.getMessage());
        return handleExceptionInternal(exception, body, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolationException(final DataIntegrityViolationException exception,
                                                                        final WebRequest request) {
        Dictionary<String, String> body = new Hashtable<>();
        body.put("message", exception.getMessage());
        return handleExceptionInternal(exception, body, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
