package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserToTeacherRequest {
    private String name;
    private String surname;
    private String title;
    private String phone;
    private String email;
    private String pbnId;
    private String jobName;
    private Integer instituteId;
}
