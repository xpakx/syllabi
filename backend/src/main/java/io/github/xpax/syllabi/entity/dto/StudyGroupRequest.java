package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class StudyGroupRequest implements Serializable {
    private String description;
    private Integer studentLimit;
    private Boolean ongoing;
    private Integer courseTypeId;
    private List<Integer> teachers;
    private String name;
}
