package io.github.xpax.syllabi.entity.dto;

import io.github.xpax.syllabi.entity.CourseType;

import java.util.List;

public interface StudyGroupDetails {
    Integer getId();
    String getName();
    String getDescription();
    Integer getStudentLimit();
    Boolean getOngoing();
    CourseType getType();
    CourseYearForPage getYear();
    List<TeacherSummary> getTeachers();
}
