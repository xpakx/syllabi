package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Admission;
import io.github.xpax.syllabi.entity.AdmissionWeight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionWeightRepository extends JpaRepository<AdmissionWeight, Integer>  {
}
