package io.github.xpax.syllabi.entity.dto;

import io.github.xpax.syllabi.entity.AdmissionWeight;


public interface AdmissionPointsSummary {
    Integer getId();
    Integer getPoints();

    AdmissionWeightSummary getWeight();
}
