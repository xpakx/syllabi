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
public class CourseLiterature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String author;
    private String title;
    private String edition;
    private String pages;
    private String description;
    private Boolean obligatory;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
