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
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String courseCode;
    private String iscedCode;
    private String erasmusCode;
    private String name;
    private Integer ects;
    private String language;

    private Boolean facultative;
    private Boolean stationary;

    private String shortDescription;
    private String description;
    private String assessmentRules;
    private String effects;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "institute_id")
    private Institute organizer;

    @JsonIgnore
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="course_program",
            joinColumns={@JoinColumn(name="course_id")},
            inverseJoinColumns={@JoinColumn(name="program_id")})
    private Set<Program> programs;

    @JsonIgnore
    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="prerequisites",
            joinColumns={@JoinColumn(name="child_id")},
            inverseJoinColumns={@JoinColumn(name="prerequisite_id")})
    private Set<Course> prerequisites;
    private String requirements;
}
