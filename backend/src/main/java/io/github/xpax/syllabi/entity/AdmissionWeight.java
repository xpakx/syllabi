package io.github.xpax.syllabi.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionWeight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private Integer weight;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admission admission;
}
