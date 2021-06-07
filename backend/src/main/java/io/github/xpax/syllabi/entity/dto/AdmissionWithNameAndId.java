package io.github.xpax.syllabi.entity.dto;

public interface AdmissionWithNameAndId {
    Integer getId();
    String getName();
    ProgramWithOnlyId getProgram();
}
