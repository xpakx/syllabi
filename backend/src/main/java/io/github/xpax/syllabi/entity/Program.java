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
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "institute_id")
    private Institute organizer;

    @JsonIgnore
    @ManyToMany(mappedBy = "programs")
    private Set<Course> courses;
}
