package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseRequest {
    private String courseCode;
    private String iscedCode;
    private String erasmusCode;
    private String name;
    private Integer ects;
    private String language;

    private Boolean facultative;
    private Boolean stationary;

    private String shortDescription;
    private String description;
    private String assessmentRules;
    private String effects;
    private String requirements;

    private Integer organizerId;
}
