package io.github.xpax.syllabi.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AdmissionsBlockedException extends RuntimeException {
    public AdmissionsBlockedException(String message) {
        super(message);
    }
}
