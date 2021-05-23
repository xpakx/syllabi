package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.CourseLiterature;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseLiteratureRepository extends JpaRepository<CourseLiterature, Integer> {
    Page<LiteratureForPage> findAllByCourseId(Integer courseId, Pageable page);
}
