package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.dto.StudentWithUserId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    boolean existsStudentByUserId(Integer userId);
    <T> Optional<T> findByUserId(Integer userId, Class<T> type);
    Long deleteByUserId(Integer userId);
    Optional<Student> getByUserId(Integer userId);

    @Query("SELECT s FROM StudyGroup sg JOIN sg.students s WHERE sg.id = :id")
    Page<StudentWithUserId> findAllStudentByGroupId(Integer id, Pageable page);

    @Query("SELECT s FROM StudyGroup sg JOIN sg.year y JOIN sg.students s WHERE y.id = :yearId")
    Page<StudentWithUserId> findAllStudentByYearId(Integer yearId, Pageable page);
}
