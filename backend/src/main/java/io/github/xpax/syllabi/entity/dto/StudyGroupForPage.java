package io.github.xpax.syllabi.entity.dto;

import io.github.xpax.syllabi.entity.CourseType;

public interface StudyGroupForPage {
    Integer getId();
    String getName();
    String getDescription();
    Integer getStudentLimit();
    Boolean getOngoing();
    CourseType getType();
}
