package io.github.xpax.syllabi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private final String token;
    private final String id;
}
