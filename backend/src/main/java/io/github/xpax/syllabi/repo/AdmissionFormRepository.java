package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.AdmissionForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionFormRepository extends JpaRepository<AdmissionForm, Integer> {
    Page<AdmissionForm> getAllByAdmissionId(Integer admissionId, Pageable page);
}
