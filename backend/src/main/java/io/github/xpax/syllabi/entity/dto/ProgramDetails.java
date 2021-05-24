package io.github.xpax.syllabi.entity.dto;

public interface ProgramDetails {
    Integer getId();
    String getName();
    String getDescription();
    InstituteForPage getOrganizer();
}
