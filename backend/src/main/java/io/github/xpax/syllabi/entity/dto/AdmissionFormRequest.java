package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AdmissionFormRequest {
    private String name;
    private String surname;
    private String documentId;
    private List<AdmissionPointRequest> points;
}
