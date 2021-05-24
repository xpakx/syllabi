package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class UpdateJobRequest implements Serializable {
    private String name;
    private Integer instituteId;
}
