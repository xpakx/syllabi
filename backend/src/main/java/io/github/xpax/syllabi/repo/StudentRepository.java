package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean existsStudentByUserId(Integer userId);
    <T> Optional<T> findByUserId(Integer userId, Class<T> type);
    Long deleteByUserId(Integer userId);
}
