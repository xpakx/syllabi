package io.github.xpax.syllabi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course parent;

    @JsonIgnore
    @ManyToMany(cascade={CascadeType.MERGE})
    @JoinTable(name="year_teacher",
            joinColumns={@JoinColumn(name="course_year_id")},
            inverseJoinColumns={@JoinColumn(name="teacher_id")})
    private Set<Teacher> coordinatedBy;

    private String assessmentRules;
    private String description;
    private String commentary;
    private Date startDate;
    private Date endDate;
}
