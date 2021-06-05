package io.github.xpax.syllabi.entity.dto;

public interface AdmissionFormSummary {
    Integer getId();
    boolean getVerified();
    boolean getAccepted();
    boolean getDiscarded();
    String getName();
    String getSurname();
    String getDocumentId();
    Integer getPointsSum();
    AdmissionWithNameAndId getAdmission();
}
