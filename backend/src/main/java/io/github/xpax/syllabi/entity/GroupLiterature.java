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
public class GroupLiterature {
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
    @JoinColumn(name = "group_id")
    private StudyGroup studyGroup;
}
