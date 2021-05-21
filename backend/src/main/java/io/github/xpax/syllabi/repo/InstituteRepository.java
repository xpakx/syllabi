package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Institute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituteRepository extends JpaRepository<Institute, Integer> {
}
