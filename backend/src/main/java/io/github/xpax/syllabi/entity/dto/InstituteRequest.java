package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class InstituteRequest {
    private String code;
    private String name;
    private String url;
    private String phone;
    private String address;
    private Integer parentId;
}
