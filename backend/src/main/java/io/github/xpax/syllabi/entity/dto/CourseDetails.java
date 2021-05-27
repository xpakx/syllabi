package io.github.xpax.syllabi.entity.dto;

import io.github.xpax.syllabi.entity.Semester;

import java.util.Set;

public interface CourseDetails {
    Integer getId();
    String getCourseCode();
    String getIscedCode();
    String getErasmusCode();
    String getName();
    Integer getEcts();
    String getLanguage();
    Boolean getFacultative();
    Boolean getStationary();
    Set<Semester> getSemesters();
    Set<CourseSummary> getPrerequisites();

    String getShortDescription();
    String getDescription();
    String getAssessmentRules();
    String getEffects();
    String getRequirements();
    InstituteForPage getOrganizer();
}
