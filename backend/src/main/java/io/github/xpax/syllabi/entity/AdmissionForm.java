package io.github.xpax.syllabi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionForm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean verified;
    private boolean accepted;
    private boolean discarded;

    private String name;
    private String surname;
    private String documentId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "admission_id")
    private Admission admission;

    @JsonIgnore
    @OneToMany(mappedBy="form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdmissionPoints> points;
}
