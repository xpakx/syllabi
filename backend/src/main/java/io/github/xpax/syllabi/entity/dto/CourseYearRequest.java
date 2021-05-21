package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CourseYearRequest implements Serializable {
    private List<Integer> coordinators;
    private String assessmentRules;
    private String description;
    private String commentary;
    private Date startDate;
    private Date endDate;
}
