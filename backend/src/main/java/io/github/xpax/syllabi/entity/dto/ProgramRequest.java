package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProgramRequest {
    private Integer organizerId;
    private String name;
    private String description;
}
