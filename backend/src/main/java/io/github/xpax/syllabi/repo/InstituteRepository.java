package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.dto.InstituteDetails;
import io.github.xpax.syllabi.entity.dto.InstituteForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Integer> {
    Page<InstituteForPage> findProjectedBy(Pageable page);

    Optional<InstituteDetails> findProjectedById(Integer id);

    Page<InstituteForPage> findByParentId(Integer instituteId, Pageable page);
}
