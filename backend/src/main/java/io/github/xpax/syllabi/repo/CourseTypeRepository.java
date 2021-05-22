package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseTypeRepository extends JpaRepository<CourseType, Integer> {
}
