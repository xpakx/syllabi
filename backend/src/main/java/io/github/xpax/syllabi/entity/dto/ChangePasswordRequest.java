package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChangePasswordRequest implements Serializable {
    private String passwordOld;
    private String password;
    private String passwordRe;
}
