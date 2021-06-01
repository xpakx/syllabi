package io.github.xpax.syllabi.entity.dto;

import lombok.Getter;
import lombok.Setter;


import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CreateAdmissionRequest {
    private Date startDate;
    private Date endDate;
    private String name;
    private Integer studentLimit;
    private List<AdmissionWeightRequest> weights;
}
