package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.dto.CourseYearForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseYearRepository extends JpaRepository<CourseYear, Integer> {
    Page<CourseYearForPage> findAllByParentId(Integer parentId, Pageable page);
}
