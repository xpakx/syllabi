package io.github.xpax.syllabi.entity.dto;

public interface LiteratureForPage {
    Integer getId();
    String getAuthor();
    String getTitle();
    String getEdition();
    String getPages();
    String getDescription();
    Boolean getObligatory();
}
