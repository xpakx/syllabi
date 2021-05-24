package io.github.xpax.syllabi.entity.dto;

public interface StudentWithUserId {
    Integer getId();
    String getName();
    String getSurname();
    UserWithOnlyId getUser();
}
