package io.github.xpax.syllabi.entity.dto;

public interface InstituteDetails {
    Integer getId();
    String getName();
    String getCode();
    String getUrl();
    String getPhone();
    String getAddress();
    InstituteForPage getParent();
}
