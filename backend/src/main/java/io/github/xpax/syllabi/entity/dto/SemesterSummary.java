package io.github.xpax.syllabi.entity.dto;

public interface SemesterSummary {
    Integer getId();
    Integer getNumber();
    String getName();
    ProgramSummary getProgram();
}
