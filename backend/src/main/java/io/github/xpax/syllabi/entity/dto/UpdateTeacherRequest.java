package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTeacherRequest {
    private String name;
    private String surname;
    private String title;
    private String phone;
    private String email;
    private String pbnId;
}
