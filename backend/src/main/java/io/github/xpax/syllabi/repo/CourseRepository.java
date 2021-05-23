package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.dto.CourseDetails;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.entity.dto.CourseSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    <T> Page<T> findAllProjectedBy(Pageable page, Class<T> type);

    @EntityGraph(value = "CourseDetails", type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"programs.id", "programs.name", "prerequisites.id", "prerequisites.name", "organizer.id", "organizer.name"})
    <T> Optional<T> findProjectedById(Integer courseId, Class<T> type);

    <T> Optional<T> getProjectedById(Integer courseId, Class<T> type);

    Page<CourseForPage> findByOrganizerId(Integer organizerId, Pageable page);
}
