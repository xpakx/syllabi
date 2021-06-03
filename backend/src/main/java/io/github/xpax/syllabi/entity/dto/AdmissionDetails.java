package io.github.xpax.syllabi.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.xpax.syllabi.entity.AdmissionWeight;
import io.github.xpax.syllabi.entity.Program;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

public interface AdmissionDetails {
    Integer getId();
    ProgramSummary getProgram();
    Date getStartDate();
    Date getEndDate();
    Integer getStudentLimit();
    boolean getClosed();
    String getName();
    List<AdmissionWeight> getWeights();
}
