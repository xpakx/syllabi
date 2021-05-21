package io.github.xpax.syllabi.entity.dto;

public interface CourseForPage {
    Integer getId();
    String getCourseCode();
    String getIscedCode();
    String getErasmusCode();
    String getName();
    Integer getEcts();
    String getLanguage();
    Boolean getFacultative();
    Boolean getStationary();
}
