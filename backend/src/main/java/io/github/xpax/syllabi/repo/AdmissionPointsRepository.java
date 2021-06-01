package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.AdmissionPoints;
import io.github.xpax.syllabi.entity.AdmissionWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdmissionPointsRepository extends JpaRepository<AdmissionPoints, Integer> {
    List<AdmissionPoints> findByFormId(Integer formId);
}