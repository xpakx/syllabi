package io.github.xpax.syllabi.entity.dto;

public interface TeacherDetails {
    Integer getId();
    String getName();
    String getSurname();
    String getTitle();
    String getPhone();
    String getEmail();
    String getPbnId();
    UserWithOnlyId getUser();
    JobSummary getJob();
}
