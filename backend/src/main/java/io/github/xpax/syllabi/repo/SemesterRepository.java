package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository  extends JpaRepository<Semester, Integer> {
}
