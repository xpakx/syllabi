package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.GroupLiterature;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupLiteratureRepository extends JpaRepository<GroupLiterature, Integer> {
    Page<LiteratureForPage> findAllByStudyGroupId(Integer studyGroupId, Pageable page);
}
