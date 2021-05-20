package io.github.xpax.syllabi.entity;

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
public class Admission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="admission_student",
            joinColumns={@JoinColumn(name="admission_id")},
            inverseJoinColumns={@JoinColumn(name="student_id")})
    private Set<Student> students;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="admission_group",
            joinColumns={@JoinColumn(name="admission_id")},
            inverseJoinColumns={@JoinColumn(name="study_group_id")})
    private Set<StudyGroup> groups;

    private Date startDate;
    private Date endDate;
}
