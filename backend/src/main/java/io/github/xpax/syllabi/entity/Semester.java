package io.github.xpax.syllabi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer number;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "semesters")
    private Set<Course> courses;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;
}
