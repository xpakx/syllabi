package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Admission;
import io.github.xpax.syllabi.entity.AdmissionForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdmissionFormRepository extends JpaRepository<AdmissionForm, Integer> {
    Page<AdmissionForm> getAllByAdmissionId(Integer admissionId, Pageable page);
    Page<AdmissionForm> getAllByAdmissionIdAndVerified(Integer admissionId, Boolean verified, Pageable page);
    List<AdmissionForm> findAllByAdmissionId(Integer admissionId);

    Optional<Admission> getByAdmissionId(Integer admissionId);

    Page<AdmissionForm> getAllByUserId(Integer userId, Pageable page);

    boolean existsAdmissinFormByUserIdAndAdmissionId(Integer userId, Integer admissionId);
}
