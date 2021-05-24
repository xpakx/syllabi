package io.github.xpax.syllabi.entity.dto;

import io.github.xpax.syllabi.entity.Role;

import java.util.List;

public interface UserWithoutPassword {
    Integer getId();
    String getUsername();
    List<Role> getRoles();
}
