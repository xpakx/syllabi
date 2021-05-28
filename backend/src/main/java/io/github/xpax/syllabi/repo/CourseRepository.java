package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    <T> Page<T> findAllProjectedBy(Pageable page, Class<T> type);

    @EntityGraph(value = "CourseDetails", type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"semesters.program.id", "semesters.program.name", "prerequisites.id", "prerequisites.name", "organizer.id", "organizer.name"})
    <T> Optional<T> findProjectedById(Integer courseId, Class<T> type);

    <T> Optional<T> getProjectedById(Integer courseId, Class<T> type);

    Page<CourseForPage> findByOrganizerId(Integer organizerId, Pageable page);

    Page<CourseForPage> findBySemestersProgramId(Integer programsId, Pageable page);

    @Query("SELECT c FROM StudyGroup sg JOIN sg.year JOIN sg.year.parent c JOIN sg.students s WHERE s.user.id = :userId")
    Page<CourseForPage> findByUserId(Integer userId, Pageable page);

    Page<CourseForPage> findBySemestersId(Integer semesterId, Pageable page);
}
