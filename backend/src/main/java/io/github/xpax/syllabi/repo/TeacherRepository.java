package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
}
