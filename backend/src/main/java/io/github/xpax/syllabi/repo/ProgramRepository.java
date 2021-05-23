package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.dto.ProgramForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Integer> {
    Page<ProgramForPage> findByOrganizerId(Integer organizerId, Pageable page);
}
