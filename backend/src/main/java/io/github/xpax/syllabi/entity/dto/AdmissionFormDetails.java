package io.github.xpax.syllabi.entity.dto;

import io.github.xpax.syllabi.entity.AdmissionPoints;

import java.util.List;

public interface AdmissionFormDetails {
    Integer getId();
    boolean getVerified();
    boolean getAccepted();
    boolean getDiscarded();
    String getName();
    String getSurname();
    String getDocumentId();
    Integer getPointsSum();
    UserWithOnlyId getUser();
    AdmissionWithNameAndId getAdmission();
    List<AdmissionPointsSummary> getPoints();
}
