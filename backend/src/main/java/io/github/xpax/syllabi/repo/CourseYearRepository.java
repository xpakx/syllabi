package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.CourseYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseYearRepository extends JpaRepository<CourseYear, Integer> {
}
