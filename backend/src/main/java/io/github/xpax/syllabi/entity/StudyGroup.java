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
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    private String description;
    private Integer studentLimit;
    private Boolean ongoing;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_year_id")
    private CourseYear year;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="group_student",
            joinColumns={@JoinColumn(name="study_group_id")},
            inverseJoinColumns={@JoinColumn(name="student_id")})
    private Set<Student> students;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_type_id")
    private CourseType type;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="group_teacher",
            joinColumns={@JoinColumn(name="study_group_id")},
            inverseJoinColumns={@JoinColumn(name="teacher_id")})
    private Set<Teacher> teachers;
}
