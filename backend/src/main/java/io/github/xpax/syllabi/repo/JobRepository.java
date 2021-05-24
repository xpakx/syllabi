package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    Optional<Job> findByTeacherUserId(Integer teacherUserId);
}
