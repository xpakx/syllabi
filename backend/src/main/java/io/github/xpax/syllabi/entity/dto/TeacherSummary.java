package io.github.xpax.syllabi.entity.dto;

public interface TeacherSummary {
    Integer getId();
    String getName();
    String getSurname();
    String getTitle();
    UserWithOnlyId getUser();
}
