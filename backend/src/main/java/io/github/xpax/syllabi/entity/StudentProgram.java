package io.github.xpax.syllabi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToMany(cascade={CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name="student_student_programs",
            joinColumns={@JoinColumn(name="student_program_id")},
            inverseJoinColumns={@JoinColumn(name="student_id")})
    private Student student;

    @JsonIgnore
    @ManyToMany(cascade={CascadeType.MERGE})
    @JoinTable(name="program_student_programs",
            joinColumns={@JoinColumn(name="student_program_id")},
            inverseJoinColumns={@JoinColumn(name="program_id")})
    private Program program;

    private Integer semester;
}
