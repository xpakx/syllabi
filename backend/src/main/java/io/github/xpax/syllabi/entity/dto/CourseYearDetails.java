package io.github.xpax.syllabi.entity.dto;

import java.util.Date;
import java.util.Set;

public interface CourseYearDetails {
    Integer getId();
    CourseSummary getParent();
    Set<TeacherSummary> getCoordinatedBy();
    String getAssessmentRules();
    String getDescription();
    String getCommentary();
    Date getStartDate();
    Date getEndDate();
}
