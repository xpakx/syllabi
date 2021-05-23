package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiteratureRequest {
    private String author;
    private String title;
    private String edition;
    private String pages;
    private String description;
    private Boolean obligatory;
}
