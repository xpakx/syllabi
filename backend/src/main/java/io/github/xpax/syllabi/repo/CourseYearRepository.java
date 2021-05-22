package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.dto.CourseYearDetails;
import io.github.xpax.syllabi.entity.dto.CourseYearForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface CourseYearRepository extends JpaRepository<CourseYear, Integer> {
    Page<CourseYearForPage> findAllByParentId(Integer parentId, Pageable page);
    Page<CourseYearForPage> findByParentIdAndEndDateAfter(Integer parentId, Date afterDate, Pageable page);

    @EntityGraph(value = "CourseYearDetails", type = EntityGraph.EntityGraphType.FETCH,
            attributePaths = {"coordinatedBy.id", "coordinatedBy.name", "coordinatedBy.surname",
                    "parent.id", "parent.name"})
    <T> Optional<T> findProjectedById(Integer id, Class<T> type);
}
