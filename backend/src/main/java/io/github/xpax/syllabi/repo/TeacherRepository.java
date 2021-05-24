package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    boolean existsTeacherByUserId(Integer userId);
}
