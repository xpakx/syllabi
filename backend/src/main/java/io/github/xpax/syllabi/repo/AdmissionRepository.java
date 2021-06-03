package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Admission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Integer> {
    Page<Admission> getAllByClosed(Boolean closed, Pageable page);
}
