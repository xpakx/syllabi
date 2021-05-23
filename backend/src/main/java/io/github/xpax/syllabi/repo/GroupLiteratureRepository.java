package io.github.xpax.syllabi.repo;

import io.github.xpax.syllabi.entity.GroupLiterature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupLiteratureRepository extends JpaRepository<GroupLiterature, Integer> {
}
