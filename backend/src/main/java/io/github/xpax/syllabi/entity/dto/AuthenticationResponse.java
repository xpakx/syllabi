package io.github.xpax.syllabi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {
    private final String token;
    private final String id;
}
