package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Admission;
import io.github.xpax.syllabi.entity.dto.AdmissionDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdmissionRepository extends JpaRepository<Admission, Integer> {
    Page<Admission> getAllByClosed(Boolean closed, Pageable page);

    @EntityGraph(value = "AdmissionDetails", type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"program", "weights"})
    Optional<AdmissionDetails> findProjectedById(Integer id);
}
