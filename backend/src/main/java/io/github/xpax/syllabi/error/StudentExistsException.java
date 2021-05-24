package io.github.xpax.syllabi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentExistsException extends RuntimeException  {
    public StudentExistsException(String message) {
        super("Student for user with id " + message + " already exists!");
    }
}
