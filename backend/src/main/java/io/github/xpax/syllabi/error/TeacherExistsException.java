package io.github.xpax.syllabi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TeacherExistsException extends RuntimeException {
    public TeacherExistsException(String message) {
        super("Teacher for user with id " + message + " already exists!");
    }

}
