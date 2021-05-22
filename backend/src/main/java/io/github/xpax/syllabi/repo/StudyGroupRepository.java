package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.dto.StudyGroupForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Integer> {
    @Query("SELECT case WHEN count(t)>0 THEN true ELSE false END FROM StudyGroup sg LEFT JOIN sg.teachers t WHERE t.user.id = :userId AND sg.id = :groupId")
    boolean existsTeacherByStudyGroupWithUserId(Integer groupId, Integer userId);

    Page<StudyGroupForPage> findAllByYearId(Integer yearId, Pageable page);
}
