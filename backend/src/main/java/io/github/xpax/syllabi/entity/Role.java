package io.github.xpax.syllabi.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements GrantedAuthority {
    public static final String USER_ADMIN = "USER_ADMIN";
    public static final String INSTITUTE_ADMIN = "INSTITUTE_ADMIN";
    public static final String TEACHER_ADMIN = "TEACHER_ADMIN";
    public static final String COURSE_ADMIN = "COURSE_ADMIN";
    public static final String STUDENT = "STUDENT";
    public static final String TEACHER = "TEACHER";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String authority;
}
