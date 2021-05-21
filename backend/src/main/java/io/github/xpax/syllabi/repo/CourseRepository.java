package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    <T> Page<T> findAllProjectedBy(Pageable page, Class<T> type);
}
