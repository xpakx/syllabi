package io.github.xpax.syllabi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "program_id")
    private Program program;

    private Date startDate;
    private Date endDate;

    private boolean closed;
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy="admission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdmissionWeight> weights;
}
