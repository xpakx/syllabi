package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.dto.TeacherDetails;
import io.github.xpax.syllabi.entity.dto.TeacherSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    boolean existsTeacherByUserId(Integer userId);
    Optional<TeacherDetails> findByUserId(Integer userId);
    Optional<Teacher> getByUserId(Integer userId);
    Long deleteByUserId(Integer userId);
    Page<TeacherSummary> findAllProjectedBy(Pageable page);
}
