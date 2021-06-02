package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentProgramRequest {
    private String name;
    private String surname;
    private String documentId;
    private Integer programId;
}
