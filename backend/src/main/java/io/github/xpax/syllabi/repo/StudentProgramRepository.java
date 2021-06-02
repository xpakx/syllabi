package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.entity.StudentProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentProgramRepository  extends JpaRepository<StudentProgram, Integer> {
}
