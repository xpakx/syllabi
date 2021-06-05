package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Admission;
import io.github.xpax.syllabi.entity.AdmissionForm;
import io.github.xpax.syllabi.entity.dto.AdmissionFormDetails;
import io.github.xpax.syllabi.entity.dto.AdmissionFormSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionFormRepository extends JpaRepository<AdmissionForm, Integer> {
    Page<AdmissionFormSummary> getAllByAdmissionId(Integer admissionId, Pageable page);
    Page<AdmissionFormSummary> getAllByAdmissionIdAndVerified(Integer admissionId, Boolean verified, Pageable page);
    List<AdmissionForm> findAllByAdmissionId(Integer admissionId);

    Page<AdmissionFormSummary> getAllByUserId(Integer userId, Pageable page);

    boolean existsAdmissionFormByUserIdAndAdmissionId(Integer userId, Integer admissionId);

    Optional<AdmissionFormDetails> findProjectedById(Integer admissionId);
}
