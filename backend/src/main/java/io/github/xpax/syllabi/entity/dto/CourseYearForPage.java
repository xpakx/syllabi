package io.github.xpax.syllabi.entity.dto;

import java.util.Date;

public interface CourseYearForPage {
    Integer getId();
    CourseSummary getParent();
    String getDescription();
    Date getStartDate();
    Date getEndDate();
}
