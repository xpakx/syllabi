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
public class AdmissionPoints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer points;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "admission_weight_id")
    private AdmissionWeight weight;

    @ManyToOne(fetch = FetchType.LAZY)
    private AdmissionForm form;
}
